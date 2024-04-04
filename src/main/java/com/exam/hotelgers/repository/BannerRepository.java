package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    //사용자 아이디로 조회
    Optional<Banner> findByTitle(String title);
}
