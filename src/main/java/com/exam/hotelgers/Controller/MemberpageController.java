package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.MenuOrder;
import com.exam.hotelgers.entity.RoomOrder;
import com.exam.hotelgers.repository.RoomOrderRepository;
import com.exam.hotelgers.repository.RoomRepository;
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

    private final RoomOrderRepository roomOrderRepository;
    private final MenuOrderService menuOrderService;
    private final ModelMapper modelMapper;

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
    public String readform(Model model, @PathVariable Long storeIdx) throws Exception {

        StoreDTO storeDTO = storeService.read(storeIdx);

        //중복 없이 객실 타입 리스트를 불러오는 쿼리문을 실행한다.
        List<RoomDTO> roomTypeList = roomService.roomTypeSearch(storeIdx);

        //매장과 룸 인덱스가 일치하는 이미지중 세부이미지들을 불러오는 쿼리문을 실행한다.
        List<ImageDTO> roomImageList = imageService.roomImageSearch(storeIdx);

        //위와 동일한데 대표이미지를 불러온다.
        ImageDTO roomMainImage = imageService.roomMainImageSearch(storeIdx);


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

        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        log.info("Detail Menu List: " + storeDTO.getDetailmenuDTOList());
        log.info("Menu Category List: " + storeDTO.getMenuCateDTOList());
        return "member/memberpage/read";
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







    @GetMapping ("/member/mypage/history")
    public String historyForm(){
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
