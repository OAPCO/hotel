package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.StoreDist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreDistRepository extends JpaRepository<StoreDist, Long> {
    //사용자 아이디로 조회

    Optional<StoreDist> findByStoreDistCode(String storeDistCode);

    Optional<StoreDist> findByStoreDistIdx(Long storeDistIdx);

    Optional<StoreDist> findByStoreDistIdx(Long storeDistIdx);


}
