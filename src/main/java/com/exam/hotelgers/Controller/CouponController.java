package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.CouponDTO;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.service.CouponService;
import com.exam.hotelgers.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CouponController {
    private final CouponService couponService;


    @PostMapping("/couponInsert")
    public String CouponRegisterProc(@RequestBody CouponDTO couponDTO) {

        Long newCoupon = couponService.register(couponDTO);

        return "redirect:/member/memberpage/index";
    }
}
