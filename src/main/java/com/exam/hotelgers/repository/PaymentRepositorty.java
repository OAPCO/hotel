package com.exam.hotelgers.repository;


import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Payment;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.RoomOrder;
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


    //매장의 결제내역 리스트
    @Query("select p from Payment p where p.storeIdx = :#{#searchDTO.storeIdx} " +
            "and p.regdate BETWEEN :#{#searchDTO.startDateTime} AND :#{#searchDTO.endDateTime}")
    Page<Payment> storesalesDateSearch(Pageable pageable, SearchDTO searchDTO);



    //내 매장의 매출 확인 쿼리들
    //연도별
    @Query("SELECT YEAR(p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "GROUP BY YEAR(p.regdate) " +
            "ORDER BY YEAR(p.regdate) ")
    List<Object[]> getYearSales(Long storeIdx);

    @Query("SELECT MONTH(p.regdate)," +
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "GROUP BY MONTH (p.regdate) " +
            "ORDER BY month (p.regdate)")
    List<Object[]> getMonthSales(Long storeIdx);


    //일별
    @Query("SELECT DAY(p.regdate),"+
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "GROUP BY day (p.regdate)" +
            "ORDER BY day (p.regdate)")
    List<Object[]> getDaySales(Long storeIdx);






    //내 총판의 매출 확인 쿼리들
    //연도별
    @Query("SELECT YEAR(p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p " +
            "WHERE p.distIdx = :distIdx " +
            "GROUP BY YEAR(p.regdate) " +
            "ORDER BY YEAR(p.regdate) ")
    List<Object[]> getDistYearSales(Long distIdx);

    @Query("SELECT MONTH(p.regdate)," +
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.distIdx = :distIdx " +
            "GROUP BY MONTH (p.regdate) " +
            "ORDER BY month (p.regdate)")
    List<Object[]> getDistMonthSales(Long distIdx);


    //일별
    @Query("SELECT DAY(p.regdate),"+
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.distIdx = :distIdx " +
            "GROUP BY day (p.regdate)" +
            "ORDER BY day (p.regdate)")
    List<Object[]> getDistDaySales(Long distIdx);
    
    
    
    

}

