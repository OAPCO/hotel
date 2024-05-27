package com.exam.hotelgers.repository;


import com.exam.hotelgers.entity.Payment;
import com.exam.hotelgers.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepositorty extends JpaRepository<Payment,Long> {

    //매장의 결제내역 리스트
    @Query("select p from Payment p where p.storeIdx = :storeIdx")
    Page<Payment> storesalesSearch(Pageable pageable, Long storeIdx);

    
    //총판의 결제내역 리스트
    @Query("select p from Payment p where p.storeIdx = :distIdx")
    Page<Payment> distsalesSearch(Pageable pageable, Long distIdx);
}

