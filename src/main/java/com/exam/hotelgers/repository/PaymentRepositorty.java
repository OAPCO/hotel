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


    //연도별
    @Query("SELECT YEAR(p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "GROUP BY YEAR(p.regdate) " +
            "ORDER BY YEAR(p.regdate) ")
    List<Object[]> getYearSales(Long storeIdx);


    @Query("SELECT "+
            "SUM(p.paymentPrice)  " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "GROUP BY YEAR(p.regdate) " +
            "ORDER BY YEAR(p.regdate) ")
    List<Object[]> getYearSales2(Long storeIdx);


    //개월별
    @Query("SELECT YEAR(p.regdate) AS year," +
            "MONTH(p.regdate) AS month, " +
            "SUM(p.paymentPrice) AS total_sales " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "GROUP BY YEAR(p.regdate), MONTH (p.regdate) " +
            "ORDER BY YEAR(p.regdate), month (p.regdate)")
    List<Object[]> getMonthSales(Long storeIdx);
    

}

