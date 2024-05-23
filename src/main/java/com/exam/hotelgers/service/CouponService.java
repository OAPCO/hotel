package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.CouponDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.CouponRepository;
import com.exam.hotelgers.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponService {
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    public Long register(CouponDTO couponDTO) {
        // memberId로 Member entity 찾기
        Optional<Member> optionalMember = memberRepository.findByMemberEmail(couponDTO.getMemberEmail());

        if (!optionalMember.isPresent()) {
            // throw exception or return, as per your requirement
            throw new IllegalArgumentException("해당하는 유저를 찾지 못했습니다");
        }

        // CouponDTO를 Coupon entity로 변환
        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);

        // 찾아낸 Member entity를 Coupon에 설정
        coupon.setMember(optionalMember.get());

        // 변환된 Coupon entity 저장
        couponRepository.save(coupon);

        return coupon.getCouponIdx();
    }
    public CouponDTO getCoupon(Long couponId){
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponId);

        // `Optional.isPresent()`를 통해 Coupon 객체가 실제로 존재하는지 확인
        if(optionalCoupon.isPresent()){
            // CouponDTO를 Coupon entity로 변환
            return modelMapper.map(optionalCoupon.get(), CouponDTO.class);
        } else {
            // 적절한 에러 처리가 필요합니다. 예를 들어 custom exception을 던질 수 있습니다.
            throw new NullPointerException("Coupon not found with id: " + couponId);
        }
    }
    public void useCoupon(Long couponIdx) {
        Optional<Coupon> optionalCoupon = couponRepository.findById(couponIdx);

        if (optionalCoupon.isPresent()) {
            Coupon coupon = optionalCoupon.get();
            coupon.setCouponState("사용 불가"); // Assuming setCouponState is a valid method
            couponRepository.save(coupon);
        }
    }
}
