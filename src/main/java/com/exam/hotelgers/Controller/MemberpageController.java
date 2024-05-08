package com.exam.hotelgers.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class MemberpageController {
    @GetMapping ("/member/mypage/mypage")
    public String mypageform(){
        return "member/mypage/mypage";
    }

    @GetMapping("/member/mypage/mypagean")
    public String mypageanform() {

        return "member/mypage/mypagean";
    }
    @GetMapping("/member/mypage/mypagepo")
    public String mypagepoform() {

        return "member/mypage/mypagepo";
    }
    @GetMapping("/member/mypage/mypagepw")
    public String mypagepwform() {

        return "member/mypage/mypagepw";
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
