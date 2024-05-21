package com.exam.hotelgers.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CouponController {
    @PostMapping("/couponInsert")
    public String CouponRegisterProc(Principal principal, Model model){


        return "member/memberpage/index";
    }
}
