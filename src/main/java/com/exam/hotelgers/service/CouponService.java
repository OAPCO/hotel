package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.CouponDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.repository.CouponRepository;
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

    public Long register(CouponDTO couponDTO) {

        Coupon coupon = modelMapper.map(couponDTO, Coupon.class);

        couponRepository.save(coupon);

        return couponRepository.save(coupon).getCouponIdx();
    }
}
