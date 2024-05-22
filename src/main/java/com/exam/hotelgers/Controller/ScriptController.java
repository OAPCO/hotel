package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ScriptController {

    private final StoreService storeService;
    private final BrandService brandService;
    private final ManagerService managerService;
    private final MemberService memberService;
    private final RoomService roomService;
    private final ImageService imageService;
    private final RoomRepository roomRepository;
    private final RoomOrderService roomOrderService;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;





    @GetMapping(value = "/selectstore", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectStore(SearchDTO searchDTO, Principal principal,Pageable pageable) throws Exception {

        log.info("ajax - selectStore Get 도착했음");

        Map<String, Object> result = new HashMap<>();

        List<BrandDTO> distOfBrand = brandService.distOfBrand(searchDTO);
        List<StoreDTO> distbrandOfStore = storeService.distbrandOfStore(searchDTO);


        result.put("distOfBrand", distOfBrand);
        result.put("distbrandOfStore", distbrandOfStore);

        return result;
    }



    @GetMapping(value = "/registerstore", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> registerStore(SearchDTO searchDTO) throws Exception {

        log.info("ajax - registerstore Get 도착했음");

        Map<String, Object> result = new HashMap<>();

        List<BrandDTO> distOfBrand = brandService.distOfBrand(searchDTO);
        List<ManagerDTO> distOfManager = managerService.distOfManager(searchDTO);

        result.put("distOfBrand", distOfBrand);
        result.put("distOfManager", distOfManager);

        return result;
    }

    @GetMapping("/emailcheck/{memberEmail}") //여기에서 중복확인
    public ResponseEntity<Integer> checkEmailDuplication(@PathVariable String memberEmail) {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setMemberEmail(memberEmail);
        int result = memberService.checkEmailDuplication(searchDTO);
        log.info("중복확인"+result+memberEmail);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/api/kakao/userInfo")
    public String receiveKakaoUserInfo(@RequestBody String userInfo, MemberDTO memberDTO, Model model) throws Exception {
        String loginInfo = memberService.kakaoregister(userInfo, memberDTO);


        String[] loginDetails = loginInfo.split(":");
        String memberEmail = loginDetails[0];
        String memberPassword = loginDetails[1];
        log.info(memberEmail + memberPassword);


            return loginInfo;
    }




    @GetMapping(value = "/emptyroom", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> hotelreadProc(SearchDTO searchDTO) throws Exception {


        log.info("서치dto의 storeIdx 확인 : "+ searchDTO.getStoreIdx());
        log.info("서치dto의 reservationDateCheckin 확인 : "+ searchDTO.getReservationDateCheckin());

        //이 두개를 변환해야함

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(searchDTO.getReservationDateCheckin(), formatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(searchDTO.getReservationDateCheckout(), formatter).atStartOfDay();
        log.info("변환된시작일"+startDate);
        log.info("변환된끝일"+endDate);

        searchDTO.setReservationDateCheckinDate(startDate);
        searchDTO.setReservationDateCheckinDate(endDate);

        List<RoomDTO> emptyRoomTypes = roomService.emptyRoomSearch(searchDTO);

        //전체 객실 중복없이 호출. 반환은 안할거임.
        List<RoomDTO> roomTypes = roomService.roomTypeSearch(searchDTO.getStoreIdx());


        //위 두 결과를 이용하여 매진 된 객실타입 목록을 만들어냄.
        List<RoomDTO> notEmptyRoomTypes = roomService.notEmptyRoomSearch(searchDTO.getStoreIdx(),emptyRoomTypes,roomTypes);


        Map<String, Object> result = new HashMap<>();

        result.put("emptyRoomTypes", emptyRoomTypes);
        result.put("notEmptyRoomTypes", notEmptyRoomTypes);


        return result;
    }


    
    //객실 생성 페이지에서 사용
    @GetMapping(value = "/roomtypeimage", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> roomTypeImageReadProc(SearchDTO searchDTO) throws Exception {

        Map<String, Object> result = new HashMap<>();
        
        List<ImageDTO> roomTypeDetailImages = imageService.roomTypeDetailImageSearch(searchDTO);
        ImageDTO roomTypeMainImage = imageService.roomTypeMainImageSearch(searchDTO);



        result.put("roomTypeDetailImages", roomTypeDetailImages);
        result.put("roomTypeMainImage", roomTypeMainImage);

        return result;
    }




    //체크인 했을 때 컨트롤러
    @GetMapping(value = "/roomcheckin")
    public void roomCheckinProc(Long storeIdx,String roomCd, Long roomorderIdx) throws Exception {

        //roomidx 찾기
        Long roomIdx = roomRepository.searchRoomIdx(roomCd,storeIdx);

        //객실, 객실주문 둘 다 체크인상태로 변경
        roomService.roomStatusUpdate2(roomIdx,roomorderIdx);
        roomOrderService.roomOrderStatusUpdate2(roomIdx,roomorderIdx);


    }


    //룸cd로 룸 찾기
    @GetMapping(value = "/roomidxfind")
    public Long findRoomIdxProc(Long storeIdx,String roomCd) throws Exception {

        log.info("스토어아ㅣㄷ"+storeIdx);
        log.info("룸시디:"+roomCd);
        //roomidx 찾기
        Long roomIdx = roomRepository.searchRoomIdx(roomCd,storeIdx);

        log.info("겱롸"+roomIdx);

        return roomIdx;
    }



    //객실생성 페이지에서 객실 타입 정보 불러오기
    @GetMapping(value = "/roomtypedata")
    public RoomDTO roomTypeDataSearch(Long storeIdx, String roomType) throws Exception {

        log.info("로그화긴@@ + " + storeIdx);
        log.info("로그화긴@@ + " + roomType);


        RoomDTO roomDTO = roomService.roomTypeSearchOne(storeIdx,roomType);

        log.info("룸디티오 머임@@" + roomDTO);

        return roomDTO;

    }



    //객실생성 페이지에서 객실 타입 정보 불러오기
    @GetMapping(value = "/roompriceupdate")
    public void roomPriceUpdate(int roomPrice, String roomType,Principal principal) throws Exception {

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        Long storeIdx = storeDTO.getStoreIdx();

        log.info("로그화긴@@ + " + storeIdx);
        log.info("로그화긴@@ + " + roomType);
        log.info("로그화긴@@ + " + roomPrice);


        roomService.roomPriceUpdate(roomPrice, roomType, storeIdx);

    }

    



}


