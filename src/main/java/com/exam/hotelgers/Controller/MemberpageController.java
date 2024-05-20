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
    public String paylogicProc(@ModelAttribute("roomOrderDTO") RoomOrderDTO roomOrderDTO,RedirectAttributes redirectAttributes,Model model){


//        //이 곳은 결제 페이지로 이용한다. 추후 결제 로직을 추가한 뒤 예약이 완료되게 변경한다.
//        //결제 했을 시 결제상태 1로 변경
//        roomOrderDTO.setPayCheck(1);
//
//        //예약된 방 하나를 상태를 3으로 바꾼다.
//        roomService.roomStatusUpdate3(roomOrderDTO);
//
//        //객실예약 추가
//        roomOrderService.register(roomOrderDTO);

        redirectAttributes.addFlashAttribute("roomOrderDTO",roomOrderDTO);

        log.info("포스트매핑 값 : " + roomOrderDTO);

        return "redirect:/member/memberpage/paypage";

    }



    @GetMapping("/member/memberpage/paypage")
    public String paypageform(RoomOrderDTO roomOrderDTO,Model model){



        log.info("겟매핑 값 : " + roomOrderDTO);

        model.addAttribute("roomOrderDTO",roomOrderDTO);

        return "member/memberpage/paypage";
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

    @GetMapping("/member/memberpage/menuorder/{storeIdx}")
    public String menuorderform(Model model, @PathVariable Long storeIdx,Principal principal) throws Exception {
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

        return "member/mypage/history";
    }

    @GetMapping("/member/mypage/info")
    public String infoForm(Principal principal, Model model) {

        Long memberIdx = Long.parseLong(principal.getName());
        MemberDTO memberDTO = memberService.memberInfoSearch(principal);
        OrderHistoryDTO orderHistory = memberpageService.getOrderHistory(memberIdx);



        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("orderHistory", orderHistory);
        log.info(memberDTO.toString()  + ": 마이페이지 유저 데이터 ");
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
    public String qnaCenterForm() {

        return "member/memberpage/qnacenter";
    }




    @GetMapping("/member/mypage/point")
    public String pointForm() {

        return "member/mypage/point";
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
