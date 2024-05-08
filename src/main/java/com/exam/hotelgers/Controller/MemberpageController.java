package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.service.MemberService;
import com.exam.hotelgers.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MemberpageController {

    private final MemberService memberService;
    private final SearchService searchService;


    @GetMapping ("/member/mypage/history")
    public String historyForm(){
        return "member/mypage/history";
    }

    @GetMapping("/member/mypage/info")
    public String infoupdateForm(MemberDTO memberDTO, Principal principal, Model model) {

        memberDTO = memberService.memberInfoSearch(principal);


        model.addAttribute("memberDTO",memberDTO);

        return "member/mypage/info";
    }
    @GetMapping("/member/mypage/point")
    public String pointForm() {

        return "member/mypage/point";
    }
    @GetMapping("/member/mypage/myqna")
    public String myqnaForm() {

        return "member/mypage/myqna";
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

    @GetMapping("/member/memberpage/index")
    public String indexform() {

        return "member/memberpage/index";
    }


    @GetMapping("/member/memberpage/test")
    public String testform() {

        return "member/memberpage/test";
    }
}
