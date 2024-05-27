package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.CouponDTO;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.service.CouponService;
import com.exam.hotelgers.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log4j2
public class CouponController {
    private final CouponService couponService;


    @PostMapping("/couponInsert")
    @ResponseBody
    public ResponseEntity<String> CouponRegisterProc(@RequestBody CouponDTO couponDTO) {
        try {
            Long newCoupon = couponService.register(couponDTO);
            return ResponseEntity.ok("쿠폰이 성공적으로 등록되었습니다.");
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/usecoupon/{couponIdx}")
    public ResponseEntity<CouponDTO> getCoupon(@PathVariable Long couponIdx) {
        CouponDTO couponDTO = couponService.getCoupon(couponIdx);
        return new ResponseEntity<>(couponDTO, HttpStatus.OK);
    }
}
