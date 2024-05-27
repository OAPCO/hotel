package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.RoomOrder;
import com.exam.hotelgers.repository.*;
import com.exam.hotelgers.repository.RoomRepository;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.service.MemberService;
import com.exam.hotelgers.service.QnaService;
import com.exam.hotelgers.service.SearchService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import com.exam.hotelgers.util.PageConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private final ReviewService reviewService;
    private final MenuSheetRepository menuSheetRepository;



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

        // 각 스토어의 평균 별점을 계산하여 추가
        for (StoreDTO store : storeList) {
            Long storeIdx = store.getStoreIdx();
            List<ReviewDTO> reviewDTOList = reviewService.findByStoreIdx(storeIdx);
            double averageRating = reviewService.calculateAverageRating(reviewDTOList);
            store.setAverageRating(averageRating); // StoreDTO에 추가적인 필드로 평균 별점 저장
        }

        model.addAttribute("storeList", storeList);

        return "member/memberpage/list";
    }


    @GetMapping("/member/memberpage/{storeIdx}")
    public String hotelreadform(Model model, @PathVariable Long storeIdx,Principal principal, RedirectAttributes redirectAttributes) throws Exception {

        if (principal == null){
            redirectAttributes.addFlashAttribute("message", "로그인을 먼저 해주세요");
            return "redirect:/member/login";
        }


        MemberDTO memberDTO = memberService.memberInfoSearch(principal);

        StoreDTO storeDTO = storeService.read(storeIdx);

        List<ReviewDTO> reviewDTOList = reviewService.findByStoreIdx(storeIdx);

        // 평균 별점 계산
        double averageRating = reviewService.calculateAverageRating(reviewDTOList);

        //중복 없이 객실 타입 리스트를 불러오는 쿼리문을 실행한다.
        List<RoomDTO> roomTypeList = roomService.roomTypeSearch(storeIdx);

        log.info("테스트123" + roomTypeList);

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
        model.addAttribute("reviewList",reviewDTOList);
        model.addAttribute("averageRating", averageRating);

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
    public String paypageform(RoomOrderDTO roomOrderDTO, Model model,Principal principal){


        MemberDTO memberDTO = memberService.memberInfoSearch(principal);

        log.info("룸오더디티오 : "+roomOrderDTO);
        model.addAttribute("roomOrderDTO",roomOrderDTO);
        model.addAttribute("memberDTO",memberDTO);

        return "member/memberpage/paypage";
    }


    @GetMapping("/member/memberpage/menupaypage")
    public String menupaypageform(@ModelAttribute("result") MenuOrderDTO menuOrderDTO,Model model,Principal principal){


        log.info("메뉴오다디티이"+menuOrderDTO);

        MemberDTO memberDTO = memberService.memberInfoSearch(principal);



        String storeName = storeRepository.findStoreName(menuOrderDTO.getStoreIdx());

        model.addAttribute("menuOrderDTO",menuOrderDTO);
        model.addAttribute("memberDTO",memberDTO);
        model.addAttribute("storeName",storeName);

        return "member/memberpage/menupaypage";
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


    @PostMapping("/menupaycheck")
    public String menupayCheckProc(@Valid MenuOrderDTO menuOrderDTO,PaymentDTO paymentDTO,RedirectAttributes redirectAttributes){



        menuOrderService.register(menuOrderDTO);

        //결제테이블 컬럼 추가
        paymentService.register(paymentDTO);



        redirectAttributes.addFlashAttribute("menuOrderDTO",menuOrderDTO);


        return "redirect:/member/mypage/history";

    }



    @GetMapping("/member/memberpage/menuordercheck")
    public String menuordercheckform(Principal principal, Model model) throws Exception {
        // Check if user is not logged in
        if (principal == null) {
            return "redirect:/member/login";
        }

        MemberDTO memberDTO = memberService.memberInfoSearch(principal);
        log.info(memberDTO.getMemberIdx());

        // 체크인 되어 있는 방 예약 내역 찾기 , status가 2여야 함 ( 체크인 중 )
        RoomOrderDTO optedRoomOrder = roomOrderService.findmemberInRoomOrder(memberDTO.getMemberIdx());

        // If alert message exists (meaning QR check-in was successful), add it to model to be displayed in view
        if(model.containsAttribute("alert")) {
            model.addAttribute("alert", model.asMap().get("alert"));
        }

        // 모든 조건 충족 시 아래 링크로 이동
        return "redirect:/member/memberpage/menuorder/" + optedRoomOrder.getRoomorderIdx();
    }





    @GetMapping("/member/memberpage/menuorder/{roomorderIdx}")
    public String menuorderform(Model model, @PathVariable Long roomorderIdx,Principal principal) throws Exception {

        log.info("일단 가져와진 루 ㅁ오더이디엑스"+roomorderIdx);

        //현재 객실 주문 번호로 체크인 한 매장번호를 구한다.
        Long storeIdx = roomOrderRepository.findStoreIdx(roomorderIdx);

        RoomOrderDTO roomOrderDTO = roomOrderService.findmemberInRoomOrder(storeIdx);

        //룸오더idx로 현재 묵고있는 store를 구한다.
        StoreDTO storeDTO = storeService.findCheckinStore(roomorderIdx);

        log.info("가져온 스토어"+storeDTO);

        if(storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/admin/distchief/store/list";
        }

        //가져온 스토어의 idx 값으로 밑에 있는 메뉴카테,디테일메뉴 등의 정보를 read 하는 메소드 실행
        StoreDTO store = storeService.read(storeDTO.getStoreIdx());

        MemberDTO memberDTO = memberService.memberInfoSearch(principal);
        log.info(memberDTO.getMemberIdx());


        RoomOrderDTO optedRoomOrder = roomOrderService.findmemberInRoomOrder(memberDTO.getMemberIdx());
        Long roomIdx = optedRoomOrder.getRoomIdx();
        log.info(roomIdx);


        log.info("메뉴카테 목록@@@ + "+ store.getMenuCateDTOList());

        model.addAttribute("store", store);
        model.addAttribute("menuCateList", store.getMenuCateDTOList());
        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("roomIdx", roomIdx);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "member/memberpage/menuorder";
    }



//    @PostMapping("/member/memberpage/menuorder")
//    public String menuorderproc(@RequestBody MenuOrderDTO menuOrderDTO,RedirectAttributes redirectAttributes) {
//        Long menuOrderId = menuOrderService.register(menuOrderDTO);
//
//
//        return "redirect:/member/memberpage/index";
//    }



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

        model.addAttribute("memberDTO", memberDTO);

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


    @GetMapping("/member/memberpage/qrcheckin")
    public String showQrCheckinForm(Principal principal, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession session) {
        if (principal == null) {
            session.setAttribute("SPRING_SECURITY_SAVED_REQUEST", request.getServletPath());
            redirectAttributes.addFlashAttribute("message", "로그인을 먼저 해주세요");
            return "redirect:/member/login";
        }

        // checkin form을 보여주는 로직
        // 예를 들어, 특정 확인 페이지를 보여줄 수도 있습니다:
        return "member/memberpage/qrcheckin";
    }

    @PostMapping("/member/memberpage/qrcheckin")
    public String qrcheckinForm(Principal principal, RedirectAttributes redirectAttributes) {
        MemberDTO memberDTO = memberService.memberInfoSearch(principal);
        RoomOrder roomOrder = roomOrderRepository.findRoomOrderForToday(memberDTO.getMemberIdx(), LocalDateTime.now());
        if(roomOrder != null){
            roomOrder.setRoomStatus(2);
            roomOrderRepository.save(roomOrder);
            redirectAttributes.addFlashAttribute("message", "QR체크인이 완료되었습니다.");
            return "redirect:/member/memberpage/index";
        } else {
            redirectAttributes.addFlashAttribute("message", "오늘 예약한 방이 없습니다");
            return "redirect:/member/memberpage/index";
        }
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
