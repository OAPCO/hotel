package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminIncomeRepository extends JpaRepository<AdminIncome,Long> {


    @Modifying
    @Query(value = "update admin_income a set a.income_price = (a.income_price*0.5) where a.payment_idx = 5", nativeQuery = true)
    void incomePriceHalfModify(Long paymentIdx);
}
