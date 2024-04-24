package com.exam.hotelgers.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class MemberpageController {
    @GetMapping ("/member/memberpage/mypage")
    public String mypageform(){
        return "member/memberpage/mypage";
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
}
