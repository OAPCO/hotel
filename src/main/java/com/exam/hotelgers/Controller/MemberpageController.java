package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.service.MemberService;
import com.exam.hotelgers.service.QnaService;
import com.exam.hotelgers.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

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


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;


    private final QnaService qnaService;

    @GetMapping("/member/memberpage/index")
    public String indexform() {

        return "member/memberpage/index";
    }
    @PostMapping("/member/memberpage/index")
    public String indexproc(@RequestParam("keyword") String keyword, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("keyword", keyword);
        return "redirect:/member/memberpage/list";
    }

    @GetMapping("/member/memberpage/list")
    public String listform(Model model, @ModelAttribute("keyword") String keyword) {
        //S3 이미지정보전달
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        // Perform the search operation with the given keyword
        List<StoreDTO> storeList = memberpageService.searchList(keyword);
        model.addAttribute("storeList", storeList);

        return "member/memberpage/list";
    }
    @GetMapping("/member/memberpage/{storeIdx}")
    public String readform(Model model, @PathVariable Long storeIdx) throws Exception {

        StoreDTO storeDTO = storeService.read(storeIdx);

        if(storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/member/memberpage/list";
        }

        model.addAttribute("storeDTO", storeDTO);
        model.addAttribute("brand", storeDTO.getBrandDTO());
        model.addAttribute("dist", storeDTO.getDistDTO());
        model.addAttribute("distChief", storeDTO.getDistDTO().getDistChiefDTO());
        model.addAttribute("manager", storeDTO.getManagerDTO());
        model.addAttribute("roomList", storeDTO.getRoomDTOList());
        model.addAttribute("menuCateList", storeDTO.getMenuCateDTOList());

        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        log.info("Detail Menu List: " + storeDTO.getDetailmenuDTOList());
        log.info("Menu Category List: " + storeDTO.getMenuCateDTOList());
        return "member/memberpage/read";
    }
    @GetMapping("/member/memberpage/roomorder/{roomIdx}")
    public String roomorderform(Model model, @PathVariable Long roomIdx) throws Exception {

        RoomDTO roomDTO = roomService.read(roomIdx);

        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);
        model.addAttribute("roomDTO", roomDTO);

        return "member/memberpage/roomorder";
    }
    @PostMapping("/member/memberpage/roomorder")
    public String roomorderproc(@Valid RoomOrderDTO roomOrderDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "입력한 정보에 오류가 있습니다.");
            return "redirect:/member/memberpage/roomorder";
        }

        Long roomOrderIdx = roomOrderService.register(roomOrderDTO);

        String message = (roomOrderIdx != null) ? "방 주문이 성공적으로 완료되었습니다." : "방 주문에 실패하였습니다.";
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/member/memberpage/index";
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






    @GetMapping("/member/memberpage/qnacenter")
    public String qnaCenterForm() {



        return "member/memberpage/qnacenter";
    }




    @GetMapping("/member/mypage/point")
    public String pointForm() {

        return "member/mypage/point";
    }
    @GetMapping("/member/mypage/myqna")
    public String myqnaForm() {

        return "member/mypage/myqna";
    }



    @GetMapping("/member/mypage/infoupdate")
    public String infoupdateForm(MemberDTO memberDTO, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);

        log.info(memberDTO);


        model.addAttribute("memberDTO",memberDTO);


        return "member/mypage/infoupdate";
    }



    @PostMapping("/member/mypage/infoupdate")
    public String infoupdateProc(MemberDTO memberDTO, SearchDTO searchDTO) {


        memberService.memberInfoUpdate(searchDTO);


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
