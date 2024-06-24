package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.repository.*;
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
import java.util.*;

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
    private final MenuOrderService menuOrderService;
    private final PaymentRepositorty paymentRepositorty;
    private final RoomOrderRepository roomOrderRepository;
    private final DistRepository distRepository;
    private final DistChiefRepository distChiefRepository;
    private final DistService distService;

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




    //set 사용해서 바꿔보기
    @GetMapping(value = "/emptyroom", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> hotelreadProc(SearchDTO searchDTO) throws Exception {


        LocalDateTime start = searchService.changeDate(searchDTO.getReservationDateCheckin());
        LocalDateTime end = searchService.changeDate(searchDTO.getReservationDateCheckout());

        searchDTO.setReservationDateCheckinDate(start);
        searchDTO.setReservationDateCheckoutDate(end);


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


            //현재 roomType을 dto에 넣고
            searchDTO.setRoomType(roomType);


            List<RoomOrderDTO> roomOrderDTOS = roomOrderService.roomOrderCheck(searchDTO);

            //기존 주문에서 중복여부가 없다면
            if(roomOrderService.roomOrderCheck(searchDTO).isEmpty()){

                //이 객실을 빈 객실 배열에 추가한다.
                passRooms.add(roomType);
}

            //기존 주문에 중복이 있다면
            else if(!roomOrderService.roomOrderCheck(searchDTO).isEmpty()){

                //이 객실을 낫엠프티 객실 배열에 추가한다.
                notEmptyRooms.add(roomType);
            }
        }


        for(String emptyRoom : emptyRooms){

            //빈 객실타입 목록과 예약가능 객실타입 목록을 비교해서 일치하는 값이 없다면
            if(!passRooms.contains(emptyRoom)){

                //최종 빈 객실타입 목록에 이걸 추가한다.
                passRooms.add(emptyRoom);
            }
        }

        log.info("패스룸:"+passRooms);
        log.info(notEmptyRooms);

        //예약가능 객실 목록을 담을 dto 배열
        List<RoomDTO> passRoomList = new ArrayList<>();
        //예약불가 객실 목록 담을 배열
        List<RoomDTO> notRoomList = new ArrayList<>();





        Iterator<String> iterator = notEmptyRooms.iterator();
        while (iterator.hasNext()) {
            String not = iterator.next();
            if (passRooms.contains(not)) {
                iterator.remove();
            }
        }

        for (String pass : passRooms){

            passRoomList.add(roomService.roomTypeSearchToTypeString(searchDTO.getStoreIdx(), pass));
        }

        if (notEmptyRooms != null){
            for (String not : notEmptyRooms){

                notRoomList.add(roomService.roomTypeSearchToTypeString(searchDTO.getStoreIdx(), not));

            }
        }


        Map<String, Object> result = new HashMap<>();

        result.put("emptyRoomTypes", passRoomList);
        result.put("notEmptyRoomTypes", notRoomList);


        return result;
    }











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

    @GetMapping(value = "/checkOutProc")
    public void checkOutProc(Long roomIdx) throws Exception {

        //- roomorder의 roomStatus는 4(종료)로 변경
        roomOrderService.roomCheckOut(roomIdx);

        //- room의 roomStatus는 이후 예약이 있을경우 1, 없을경우 0으로 변경
        if(roomOrderRepository.roomSearch(roomIdx)==null){
            log.info("에약이 없음");
            roomService.roomCheckOutEmpty(roomIdx);
        }
        else if (roomOrderRepository.roomSearch(roomIdx)!=null){
            log.info("에약이 있음");
            roomService.roomCheckOut(roomIdx);
        };
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


    @GetMapping(value = "/menuOrderStatusChange")
    public void menuOrderStatusChange(Long menuorderIdx, String orderStatus) throws Exception {


        menuOrderService.menuOrderStatusChange(menuorderIdx,orderStatus);
    }




    @GetMapping(value = "/findPaymentData")
    public List<String> findPaymentData(Long storeIdx,Long distIdx) throws Exception {

        log.info("스토어아ㅣㄷ"+storeIdx);
        log.info("룸시디:"+distIdx);


        List<String> names = new ArrayList<>();

        
        //idx로 이름 찾기
        names.add(distRepository.finddistName(distIdx));
        names.add(storeRepository.findStoreName(storeIdx));

        log.info(names);
        


        return names;
    }



    @GetMapping(value = "/distsalessearch" , consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Page<PaymentDTO> distSalesProc(@PageableDefault(page=1) Pageable pageable, SearchDTO searchDTO,Principal principal, Model model) throws Exception {


        log.info("post 드러옴");


        Page<PaymentDTO> paymentDTOS = paymentService.distPaymentlistSearch(pageable, searchDTO, principal);
        Map<String, Integer> pageinfo = PageConvert.Pagination(paymentDTOS);


        return paymentDTOS;
    }











}


