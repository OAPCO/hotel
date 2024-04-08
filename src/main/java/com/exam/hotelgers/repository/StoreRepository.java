package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    //사용자 아이디로 조회
    Optional<Store> findByStoreCd(String storeCd);

    Optional<Store> findByStoreIdx(Long storeIdx);



}
