package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.MenuOrder;
import com.exam.hotelgers.entity.RoomOrder;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.*;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.service.MemberService;
import com.exam.hotelgers.service.QnaService;
import com.exam.hotelgers.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import com.exam.hotelgers.util.PageConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MemberpageController {
    private final MemberpageService memberpageService;
    private final MemberService memberService;
    private final SearchService searchService;
    private final StoreService storeService;
    private final DistService distService;
    private final DistChiefService distChiefService;
    private final RoomService roomService;
    private final RoomOrderService roomOrderService;
    private final RoomRepository roomRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final MenuOrderRepository menuOrderRepository;
    private final RoomOrderRepository roomOrderRepository;
    private final MenuOrderService menuOrderService;
    private final ModelMapper modelMapper;
    private final StoreRepository storeRepository;
    private final PaymentService paymentService;
    private final NoticeService noticeService;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;


    private final QnaService qnaService;

    @GetMapping("/member/memberpage/index")
    public String indexform(Model model) {
        List<StoreDTO> recommendedStores = storeService.getRandomRecommendedStores();
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        model.addAttribute("recommendedStoreList", recommendedStores);
        return "member/memberpage/index";
    }




    @PostMapping("/member/memberpage/index")
    public String indexproc(@RequestParam("keyword") String keyword, @RequestParam(name="facilities", required=false) String[] facilities, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("keyword", keyword);
        if (facilities != null) {
            redirectAttributes.addFlashAttribute("facilities", String.join(",", facilities));
        }
        return "redirect:/member/memberpage/list";
    }

    @GetMapping("/member/memberpage/list")
    public String listform(Model model, @ModelAttribute("keyword") String keyword, @ModelAttribute("facilities") String facilities) {
        //S3 이미지정보전달
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        // Perform the search operation with the given keyword and facilities
        List<StoreDTO> storeList = memberpageService.searchList(keyword, facilities);
        model.addAttribute("storeList", storeList);

        return "member/memberpage/list";
    }

    @GetMapping("/member/memberpage/{storeIdx}")
    public String hotelreadform(Model model, @PathVariable Long storeIdx,Principal principal) throws Exception {


        MemberDTO memberDTO = memberService.memberInfoSearch(principal);

        StoreDTO storeDTO = storeService.read(storeIdx);

        //중복 없이 객실 타입 리스트를 불러오는 쿼리문을 실행한다.
        List<RoomDTO> roomTypeList = roomService.roomTypeSearch(storeIdx);

        //매장과 룸 인덱스가 일치하는 이미지중 세부이미지들을 불러오는 쿼리문을 실행한다.
        List<ImageDTO> roomImageList = imageService.roomImageSearch(storeIdx);


        //객실 대표이미지를 불러온다.
        List<ImageDTO> roomMainImageList = imageService.roomMainImageSearch(storeIdx);


        //불러온 객실 타입 열거형 종류들을 String 배열 형태로 볁환한다.
        //이렇게 만든 roomTypes 변수는 모델에 담아 보낸 뒤 뷰에서 이미지 리스
        String[] roomTypes = roomTypeList.stream()
                .map(room -> room.getRoomType().toString())
                .toArray(String[]::new);

        for (String roomType : roomTypes) {
            log.info(roomType);
        }


        if(storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/member/memberpage/list";
        }
        List<RoomOrder> roomOrderList = roomOrderRepository.findByStoreIdx(storeIdx);

        List<RoomOrderDTO> roomOrderDTOList = roomOrderList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("roomOrderList", roomOrderDTOList);
        model.addAttribute("storeDTO", storeDTO);
        model.addAttribute("brand", storeDTO.getBrandDTO());
        model.addAttribute("dist", storeDTO.getDistDTO());
        model.addAttribute("distChief", storeDTO.getDistDTO().getDistChiefDTO());
        model.addAttribute("manager", storeDTO.getManagerDTO());
        model.addAttribute("roomList", storeDTO.getRoomDTOList());
        model.addAttribute("menuCateList", storeDTO.getMenuCateDTOList());
        model.addAttribute("roomOrderList",roomOrderList);

        model.addAttribute("roomTypeList",roomTypeList);
        model.addAttribute("roomTypes",roomTypes);
        model.addAttribute("roomImageList",roomImageList);
        model.addAttribute("roomMainImageList",roomMainImageList);



        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);



        return "member/memberpage/read";
    }








    @PostMapping("/member/memberpage/paypage")
    public String paylogicProc(RoomOrderDTO roomOrderDTO,RedirectAttributes redirectAttributes,Model model,Principal principal){



        redirectAttributes.addFlashAttribute("roomOrderDTO",roomOrderDTO);


        return "redirect:/member/memberpage/paypage";

    }



    @GetMapping("/member/memberpage/paypage")
    public String paypageform(RoomOrderDTO roomOrderDTO,Model model,Principal principal){


        MemberDTO memberDTO = memberService.memberInfoSearch(principal);


        model.addAttribute("roomOrderDTO",roomOrderDTO);
        model.addAttribute("memberDTO",memberDTO);

        return "member/memberpage/paypage";
    }



    @PostMapping("/paycheck")
    public String payCheckProc(RoomOrderDTO roomOrderDTO,PaymentDTO paymentDTO,RedirectAttributes redirectAttributes,Model model,Principal principal){

        
        //날짜변환

        LocalDateTime start = searchService.changeDate(roomOrderDTO.getReservationDateCheckin());
        LocalDateTime end = searchService.changeDate(roomOrderDTO.getReservationDateCheckout());

        roomOrderDTO.setReservationDateCheckinDate(start);
        roomOrderDTO.setReservationDateCheckoutDate(end);


        //예약된 방 하나를 상태를 1로 바꾼다.
        roomService.roomStatusUpdate1(roomOrderDTO);

        //객실예약 추가
        roomOrderService.register(roomOrderDTO);

        //결제테이블 컬럼 추가
        paymentService.register(paymentDTO);



        redirectAttributes.addFlashAttribute("roomOrderDTO",roomOrderDTO);


        return "redirect:/member/mypage/history";

    }







    //인덱스에서 메뉴 주문 버튼 누를 시 로그인 여부/예약 여부 확인해서 링크 타도록 변경
    @GetMapping("/member/memberpage/menuordercheck")
    public String menuordercheckform(Principal principal) throws Exception {
        // Check if user is not logged in
        if (principal == null) {
            return "redirect:/member/login";
        }

        MemberDTO memberDTO = memberService.memberInfoSearch(principal);
        log.info(memberDTO.getMemberIdx());

        // Check if logged in user has reservation
        Optional<RoomOrder> optedRoomOrder = roomOrderRepository.findByMemberIdx(memberDTO.getMemberIdx());

        if (!optedRoomOrder.isPresent()) {
            return "redirect:/member/memberpage/menuordererror";
        }

        Long storeIdx = optedRoomOrder.get().getStoreIdx();
        log.info(storeIdx  + ": 예약한 방 번호입니다 ");
//        모든 조건 충족 시 아래 링크로 이동
        return "redirect:/member/memberpage/menuorder/" + storeIdx;
    }


//    @GetMapping("/member/memberpage/roomordercheck")
//    public String Roomordercheckform(Principal principal) throws Exception {
//        // 유저의 로그인 상태 확인
//        if (principal == null) {
//            return "redirect:/member/login";
//        }
//
//        MemberDTO memberDTO = memberService.memberInfoSearch(principal);
//        log.info(memberDTO.getMemberIdx());
//
//        // 해당 회원이 예약을 했다면
//        Optional<RoomOrder> optedRoomOrder = roomOrderRepository.findByMemberIdx(memberDTO.getMemberIdx());
//
//        if (!optedRoomOrder.isPresent()) {
//
//            return "redirect:/member/memberpage/Roomordererror";
//        }
//
//        //오늘 날짜와 예약된 날짜 체크. 변환 후 시행
//        RoomOrder roomOrder = optedRoomOrder.get();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Adjust this based on your date format
//        LocalDate checkinDate = LocalDate.parse(roomOrder.getCheckinTime(), formatter);
//        LocalDate checkoutDate = LocalDate.parse(roomOrder.getCheckoutTime(), formatter);
//        LocalDate now = LocalDate.now();
//
//        if ((now.isEqual(checkinDate) || now.isAfter(checkinDate)) && now.isBefore(checkoutDate)) {
//            roomOrder.setRoomStatus(2);
//            roomOrderRepository.save(roomOrder);
//        }
//
//        return "redirect://member/mypage/history";
//    }


    @GetMapping("/member/memberpage/menuorder/{roomorderIdx}")
    public String menuorderform(Model model, @PathVariable Long roomorderIdx,Principal principal) throws Exception {

        //현재 객실 주문 번호로 체크인 한 매장번호를 구한다.
        Long storeIdx = roomOrderRepository.findStoreIdx(roomorderIdx);


        //위에서 구한 매장번호로 storeDTO 구한다.
        StoreDTO storeDTO = storeService.read(storeIdx);

        if(storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/admin/distchief/store/list";
        }

        MemberDTO memberDTO = memberService.memberInfoSearch(principal);
        log.info(memberDTO.getMemberIdx());
        Optional<RoomOrder> optedRoomOrder = roomOrderRepository.findByMemberIdx(memberDTO.getMemberIdx());
        Long roomIdx = optedRoomOrder.get().getRoomIdx();
        log.info(roomIdx);


        model.addAttribute("store", storeDTO);
        model.addAttribute("brand", storeDTO.getBrandDTO());
        model.addAttribute("dist", storeDTO.getDistDTO());
        model.addAttribute("distChief", storeDTO.getDistDTO().getDistChiefDTO());
        model.addAttribute("manager", storeDTO.getManagerDTO());
        model.addAttribute("roomList", storeDTO.getRoomDTOList());
        model.addAttribute("menuCateList", storeDTO.getMenuCateDTOList());
        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("roomIdx", roomIdx);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        log.info("Detail Menu List: " + storeDTO.getDetailmenuDTOList());
        log.info("Menu Category List: " + storeDTO.getMenuCateDTOList());
        return "member/memberpage/menuorder";
    }



    @PostMapping("/member/memberpage/menuorder")
    public String menuorderproc(@RequestBody MenuOrderDTO menuOrderDTO) {
        Long menuOrderId = menuOrderService.register(menuOrderDTO);
        return "redirect:/member/memberpage/index";
    }



    @GetMapping("/member/memberpage/menuordererror")
    public String errorform(){

        return "member/memberpage/menuordererror";
    }


    @PostMapping("/member/memberpage/read")
    public String readProc(Model model, @PathVariable Long storeIdx) throws Exception {

        StoreDTO storeDTO = storeService.read(storeIdx);


        if(storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/member/memberpage/list";
        }



        //호텔예약페이지

        model.addAttribute("storeDTO",storeDTO);


        return "member/memberpage/index";
    }





    private RoomOrderDTO convertToDTO(RoomOrder roomOrder) {
        return modelMapper.map(roomOrder, RoomOrderDTO.class);
    }

    @GetMapping("/member/memberpage/roomorder/{roomIdx}")
    public String roomorderform(Principal principal,Model model, @PathVariable Long roomIdx) throws Exception {

        RoomDTO roomDTO = roomService.read(roomIdx);

        MemberDTO memberDTO = memberService.memberInfoSearch(principal);

        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        model.addAttribute("roomDTO", roomDTO);
        model.addAttribute("memberDTO", memberDTO);

        return "member/memberpage/roomorder";
    }


    @PostMapping("/member/memberpage/roomorder")
    public String roomorderform2(Principal principal,Model model, RoomOrderDTO roomOrderDTO) throws Exception {

        roomOrderService.register(roomOrderDTO);


        return "member/memberpage/roomorder";
    }






//제공되는 값이 null이어도 페이지가 표시되도록 수정
    @GetMapping ("/member/mypage/history")
    public String historyForm(MemberDTO memberDTO, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);
        if(memberDTO == null) {
            // Handle null memberDTO appropriately.
        } else {
            OrderHistoryDTO orderHistory = memberpageService.getOrderHistory(memberDTO.getMemberIdx());
            if(orderHistory == null) {
                // Handle null orderHistory appropriately.
            } else {
                model.addAttribute("orderHistory", orderHistory);
            }
        }
        log.info(memberDTO.toString()  + ": 마이페이지 유저 데이터 ");
        // Always adding the memberDTO, even if it's null. The view should handle this appropriately.
        model.addAttribute("memberDTO", memberDTO);

        //회원이 현재 체크인 되어있는 방 주문을 찾는다.
        RoomOrderDTO roomOrderDTO = roomOrderService.findmemberInRoomOrder(memberDTO.getMemberIdx());
        model.addAttribute("roomOrderDTO", roomOrderDTO);
        log.info("묵고있는방확인@@ " + roomOrderDTO);


        //출력에 필요한 정보 매장명,룸명을 따로 구해서 보낸다.
        String storeName = storeRepository.findStoreRoomOrderIdx(roomOrderDTO.getRoomorderIdx());
        String roomName = roomRepository.findRoomRoomOrderIdx(roomOrderDTO.getRoomorderIdx());
        model.addAttribute("isstoreName", storeName);
        model.addAttribute("isroomName", roomName);

        log.info("새로추가한거",storeName,roomName);


        return "member/mypage/history";
    }

    @GetMapping("/member/mypage/info")
    public String infoForm(MemberDTO memberDTO, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);

        log.info(memberDTO);


        model.addAttribute("memberDTO",memberDTO);

        return "member/mypage/info";
    }

    @GetMapping("/member/memberpage/question")
    public String questionForm(MemberDTO memberDTO, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);

        log.info(memberDTO);


        model.addAttribute("memberDTO",memberDTO);

        return "member/memberpage/question";
    }


    @PostMapping("/member/memberpage/question")
    public String questionProc(QnaDTO qnaDTO) {


        qnaService.register(qnaDTO);


        return "redirect:/member/mypage/myqna";
    }



    @GetMapping("/member/mypage/myqna")
    public String myqnaForm(MemberDTO memberDTO, @PageableDefault(page = 1)Pageable pageable, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);

        Page<QnaDTO> qnaDTOS = qnaService.myQnalist(pageable,memberDTO.getMemberIdx());

        Map<String, Integer> pageinfo = PageConvert.Pagination(qnaDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", qnaDTOS);


        return "member/mypage/myqna";
    }





    @GetMapping("/member/memberpage/qnacenter")
    public String qnaCenterForm(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<NoticeDTO> noticeDTOS = noticeService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(noticeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", noticeDTOS);
        return "member/memberpage/qnacenter";
    }

    @GetMapping("/member/memberpage/announcement")
    public String announcementForm(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<NoticeDTO> noticeDTOS = noticeService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(noticeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", noticeDTOS);
        return "member/memberpage/announcement";
    }


    @GetMapping("/member/mypage/point")
    public String pointForm(MemberDTO memberDTO, Principal principal, Model model) {
        memberDTO = memberService.memberPointSearch(principal);

        model.addAttribute("memberDTO",memberDTO);
        return "member/mypage/point";
    }


    @GetMapping("/member/mypage/point1")
    public String pointForm1(MemberDTO memberDTO, Principal principal, Model model) {
        memberDTO = memberService.memberPointSearch(principal);

        model.addAttribute("memberDTO",memberDTO);
        return "member/mypage/point1";
    }

    @GetMapping("/member/mypage/infoupdate")
    public String infoupdateForm(MemberDTO memberDTO, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);



        model.addAttribute("memberDTO",memberDTO);


        return "member/mypage/infoupdate";
    }



    @PostMapping("/member/mypage/infoupdate")
    public String infoupdateProc(MemberDTO memberDTO, SearchDTO searchDTO) {


        memberService.memberInfoUpdate(searchDTO);


        return "redirect:/logout";
    }


    //삭제페이지get
    @GetMapping("/member/mypage/withdraw")
    public String withdrawForm(MemberDTO memberDTO, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);


        model.addAttribute("memberDTO",memberDTO);


        return "member/mypage/withdraw";
    }



    //삭제post
    @PostMapping("/member/mypage/withdraw")
    public String withdrawProc(MemberDTO memberDTO, SearchDTO searchDTO) {

        log.info("들어온 idx@@@@ " + searchDTO.getMemberIdx());
        log.info("들어온 패스어드"+ searchDTO.getPassword());


        memberService.memberInfoDelete(searchDTO);


        return "redirect:/logout";
    }

    @GetMapping("/member/mypage/pwchange")
    public String pwchangeForm() {

        return "member/mypage/pwchange";
    }
    @PostMapping("/member/mypage/pwchange")
    public String pwchangeproc(Principal principal, String currentPassword, String newPassword) {

        log.info("현재비밀번호"+currentPassword+"새비밀번호"+newPassword);
        int result=memberService.changePassword(currentPassword,newPassword,principal);
        log.info("결과값"+result);

        return "member/mypage/pwchange";
    }




    @GetMapping ("/member/memberpage/koreafood")
    public String koreafoodform(){
        return "member/memberpage/koreafood";
    }

    @GetMapping ("/member/memberpage/basket")
    public String basketform(){
        return "member/memberpage/basket";
    }

    @GetMapping ("/member/memberpage/roomservice")
    public String roomserviceform(){
        return "member/memberpage/roomservice";
    }





}
