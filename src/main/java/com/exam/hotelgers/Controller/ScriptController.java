package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.repository.PaymentRepositorty;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.repository.StoreRepository;
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
    private final SearchService searchService;
    private final PaymentService paymentService;
    private final StoreRepository storeRepository;
    private final PaymentRepositorty paymentRepositorty;

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

        log.info("브랜드 왓는지화긴@@@ " + distOfBrand);
        log.info("서치dto 화긴@@ " + searchDTO);
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


        LocalDateTime start = searchService.changeDate(searchDTO.getReservationDateCheckin());
        LocalDateTime end = searchService.changeDate(searchDTO.getReservationDateCheckout());

        searchDTO.setReservationDateCheckinDate(start);
        searchDTO.setReservationDateCheckoutDate(end);

        log.info("날짜로그1 : "+searchDTO.getReservationDateCheckinDate());
        log.info("날짜로그2 : "+searchDTO.getReservationDateCheckoutDate());

        //예약 가능한 객실 타입의 목록
        List<String> passRooms = new ArrayList<>();

        //비어있지 않은 객실 타입 목록
        List<String> notEmptyRooms = new ArrayList<>();

        //전체 객실 타입 목록
        List<String> allRooms = roomRepository.roomTypeStringSearch(searchDTO.getStoreIdx());

        //빈 객실 타입의 목록
        List<String> emptyRooms = roomRepository.searchEmptyRoom(searchDTO.getStoreIdx());



        //전체객실 숫자만큼 반복
        for(String roomType : allRooms){


            //룸타입을 셋 해주고
            searchDTO.setRoomType(roomType);


            List<RoomOrderDTO> roomOrderDTOS = roomOrderService.roomOrderCheck(searchDTO);
            log.info(searchDTO.getRoomType() + "의 룸오더 체크하기@@@ 결과값 : "+ roomOrderDTOS);

            //기존 주문에서 중복여부가 없다면
            if(roomOrderService.roomOrderCheck(searchDTO).isEmpty()){

                //이 객실을 빈 객실 배열에 추가할그야
                passRooms.add(roomType);
            }

            //기존 주문에 중복이 있다면
            else if(!roomOrderService.roomOrderCheck(searchDTO).isEmpty()){

                //이 객실을 낫엠프티 객실 배열에 추가할그야
                notEmptyRooms.add(roomType);
                log.info("기존 객실 주문에 중복 o");
            }
        }


        for(String emptyRoom : emptyRooms){

            //빈 객실타입 목록과 예약가능 객실타입 목록을 비교해서 일치하는 값이 없다면
            if(!passRooms.contains(emptyRoom)){

                //최종 빈 객실타입 목록에 이걸 추가한다.
                passRooms.add(emptyRoom);
            }
        }

        log.info(passRooms);
        log.info(notEmptyRooms);

        //예약가능 객실 목록을 담을 dto 배열
        List<RoomDTO> passRoomList = new ArrayList<>();
        //예약불가 객실 목록 담을 배열
        List<RoomDTO> notRoomList = new ArrayList<>();
        

        for (String pass : passRooms){

            passRoomList.add(roomService.roomTypeSearchToTypeString(searchDTO.getStoreIdx(), pass));
        }

        for (String not : notEmptyRooms){

            notRoomList.add(roomService.roomTypeSearchToTypeString(searchDTO.getStoreIdx(), not));
        }




        log.info("예약가능객실 : "+passRooms);
        log.info("안 빈 객실 : "+notEmptyRooms);
        log.info("전체 객실 : "+allRooms);
        log.info("빈 객실 타입 : "+emptyRooms);
        log.info("최종통과객실 : "+passRoomList);
        log.info("최종불통객실 : "+notRoomList);
        

        Map<String, Object> result = new HashMap<>();

        result.put("emptyRoomTypes", passRoomList);
        result.put("notEmptyRoomTypes", notRoomList);


        return result;
    }






//    @GetMapping(value = "/distsales", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
//    public Page<PaymentDTO> distSales(@PageableDefault(page = 1) Pageable pageable,SearchDTO searchDTO) throws Exception {
//
//
//        LocalDateTime start = searchService.changeDate(searchDTO.getStartDate());
//        LocalDateTime end = searchService.changeDate(searchDTO.getEndDate());
//
//        searchDTO.setStartDateTime(start);
//        searchDTO.setEndDateTime(end);
//
//
//        Page<PaymentDTO> paymentDTOS = paymentService.searchList(pageable,searchDTO);
//        Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);
//
//
//        return paymentDTOS;
//    }






    //객실 생성 페이지에서 사용
    @GetMapping(value = "/roomtypeimage", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> roomTypeImageReadProc(SearchDTO searchDTO) throws Exception {


        log.info("화아긴"+searchDTO.getRoomType());
        log.info(searchDTO.getStoreIdx());

        Map<String, Object> result = new HashMap<>();
        
        List<ImageDTO> roomTypeDetailImages = imageService.roomTypeDetailImageSearch(searchDTO);
        ImageDTO roomTypeMainImage = imageService.roomTypeMainImageSearch(searchDTO);

        log.info("roomTypeDetailImages : "+ roomTypeDetailImages);
        log.info("roomTypeMainImage : "+ roomTypeMainImage);

        result.put("roomTypeDetailImages", roomTypeDetailImages);
        result.put("roomTypeMainImage", roomTypeMainImage);

        return result;
    }




    //체크인 했을 때 컨트롤러
    @GetMapping(value = "/roomcheckin")
    public void roomCheckinProc(Long storeIdx,String roomCd, Long roomorderIdx) throws Exception {

        //roomidx 찾기
        Long roomIdx = roomRepository.searchRoomIdx(roomCd,storeIdx);

        //현재 시간을 구한다.
        LocalDateTime checkinTime = LocalDateTime.now();

        //객실, 객실주문 둘 다 체크인상태로 변경
        roomService.roomStatusUpdate2(roomIdx,roomorderIdx);
        roomOrderService.roomOrderStatusUpdate2(roomIdx,roomorderIdx,checkinTime);


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





    @GetMapping(value = "/storecount")
    public int distOfStoreCount(Long distIdx) throws Exception {

        int count = storeRepository.findDistOfStoreCount(distIdx);

        log.info("카운트 : "+count);

        return storeRepository.findDistOfStoreCount(distIdx);

    }


    
    
    
    
    
    //선택한 총판에 맞는 각 매출 목록을 가져오는 매핑들
    @GetMapping(value = "/distyearsales")
    public Object[][] distYearSalesSearch(Long distIdx) throws Exception {

        Object[][] yearlySales = paymentService.getDistYearlySales(distIdx);


        return yearlySales;

    }

    @GetMapping(value = "/distmonthsales")
    public Object[][] distMonthSalesSearch(Long distIdx) throws Exception {

        Object[][] monthSales = paymentService.getDistMonthSales(distIdx);


        return monthSales;

    }

    @GetMapping(value = "/distdaysales")
    public Object[][] distDaySalesSearch(Long distIdx) throws Exception {

        Object[][] daySales = paymentService.getDistDaySales(distIdx);


        return daySales;

    }




    //선택한 매장에 맞는 각 매출 목록을 가져오는 매핑들
    @GetMapping(value = "/storeyearsales")
    public Object[][] storeYearSalesSearch(Long storeIdx) throws Exception {

        Object[][] yearlySales = paymentService.getYearlySales(storeIdx);


        return yearlySales;

    }

    @GetMapping(value = "/storemonthsales")
    public Object[][] storeMonthSalesSearch(Long storeIdx) throws Exception {

        Object[][] monthSales = paymentService.getMonthSales(storeIdx);


        return monthSales;

    }

    @GetMapping(value = "/storedaysales")
    public Object[][] storeDaySalesSearch(Long storeIdx) throws Exception {

        Object[][] daySales = paymentService.getDaySales(storeIdx);


        return daySales;

    }
    
    

    
    
    @GetMapping(value = "/summarymodify")
    public void storeSummary(String storeSummary,Long storeIdx) throws Exception {
        storeService.storeSummaryUpdate(storeSummary,storeIdx);
    }

    @GetMapping(value = "/chargemodify")
    public void chargemodify(double cancelCharge,Long storeIdx) throws Exception {
        storeService.cancelChargeUpdate(cancelCharge,storeIdx);
    }

    @GetMapping(value = "/storeMessagemodify")
    public void storeMessage(String storeMessage,Long storeIdx) throws Exception {
        storeService.storeMessageUpdate(storeMessage,storeIdx);
    }

    @GetMapping(value = "/storeCheckinTimemodify")
    public void storeCheckinTime(String storeCheckinTime,Long storeIdx) throws Exception {
        storeService.storeCheckinTimeUpdate(storeCheckinTime,storeIdx);
    }

    @GetMapping(value = "/storeCheckoutTimemodify")
    public void storeCheckoutTime(String storeCheckoutTime,Long storeIdx) throws Exception {
        storeService.storeCheckoutTimeUpdate(storeCheckoutTime,storeIdx);
    }

    @GetMapping(value = "/roomcountadd")
    public void roomcountadd(Long storeIdx) throws Exception {
        storeService.roomCardAdd(storeIdx);
    }


    //객실 예약 취소
    @GetMapping(value = "/roomorderCancel")
    public void roomorderCancel(Long roomorderIdx, @RequestParam("reservationDateCheckinDate") String reservationDateCheckin) throws Exception {

        log.info("들어옴:" + reservationDateCheckin);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        //        체크인 희망 날짜
        LocalDate checkinDate = LocalDate.parse(reservationDateCheckin,formatter);
        //        오늘날짜
        LocalDate today = LocalDate.now();

        log.info("1번"+checkinDate);
        log.info("2번"+today);

        //우선 roomorderIdx로 payment 컬럼을 조회하고
        PaymentDTO paymentDTO = paymentService.roomOrderByPaymentSearch(roomorderIdx);

        log.info("시작페이먼트:"+paymentDTO);


        // 체크인 희망 날짜가 오늘이라면 수수료 물어내야지?
        if (checkinDate.equals(today)) {

            log.info("들어옴");


            //paymentDTO에 해당하는 매장의 취소수수료를 파악해야함
            Long storeIdx = paymentDTO.getStoreIdx();

            //수수료
            double charge = storeRepository.searchStoreCharge(storeIdx);

            //수수료*기존 결제액 구하기
            double newPrice = paymentDTO.getPaymentPrice() * charge;
            int priceInt = (int) newPrice;
            paymentDTO.setPaymentPrice(priceInt);
            
            //당일취소 상태로 변경
            paymentDTO.setPaymentStatus(2);

            //payment 생성
            paymentService.register(paymentDTO);


        }

        else{
            //roomorderIdx에 해당하는 payment 컬럼의 status를 1(결제취소)로 변경한다.
            paymentService.paymentCancel(roomorderIdx);
        }


        //객실예약 컬럼 삭제
        roomOrderService.roomOrderDelete(roomorderIdx);









    }


    @PostMapping(value = "/storeImageDelete")
    public void storeImageDelete(@RequestParam("imgFile") MultipartFile imgFile, @RequestParam("storeIdx") Long storeIdx) throws Exception {
        storeService.imageModify(imgFile,storeIdx);
    }








}


