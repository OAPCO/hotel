package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Banner;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {


    Optional<Brand> findByBrandIdx(Long brandIdx);
    Optional<Brand> findByBrandCd(String brandCd);

}
