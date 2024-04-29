package com.exam.hotelgers.repository;


import com.exam.hotelgers.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepositorty extends JpaRepository<Payment,Long> {
    Optional<Payment> findByPaymentNo(String paymentNo);

    Optional<Payment> findByPaymentIdx(Long paymentIdx);
}

