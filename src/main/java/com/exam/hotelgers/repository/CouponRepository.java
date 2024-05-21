package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long> {
    List<Coupon> findByMemberEmail(String email);
}
