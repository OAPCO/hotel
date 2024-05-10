package com.exam.hotelgers.repository;

import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Basket;
import com.exam.hotelgers.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket,Long> {


    Optional<Basket> findByBasketCd(String basketCd);

    Optional<Basket> findByBasketIdx(Long basketIdx);



    @Query("select b from Basket b where (b.memberIdx = :memberIdx)")
    Page<Basket> findByMemberCart(Long memberIdx,Pageable pageable);









}