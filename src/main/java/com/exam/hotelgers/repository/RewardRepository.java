package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward,Long> {
    List<Reward> findByMemberEmail(String email);
}
