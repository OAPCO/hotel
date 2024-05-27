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


//    @Query(value = "SELECT o FROM RoomOrder o " +
//            "LEFT JOIN Room r ON r.store.storeIdx = o.storeIdx " +
//            "WHERE o.storeIdx = :#{#searchDTO.storeIdx} " +
//            "and o.roomOrderType = :#{#searchDTO.roomType} " +
//            "AND (:#{#searchDTO.reservationDateCheckinDate} BETWEEN o.reservationDateCheckinDate AND o.reservationDateCheckoutDate " +
//            "OR :#{#searchDTO.reservationDateCheckoutDate} BETWEEN o.reservationDateCheckinDate AND o.reservationDateCheckoutDate " +
//            "OR (o.reservationDateCheckinDate BETWEEN :#{#searchDTO.reservationDateCheckinDate} AND :#{#searchDTO.reservationDateCheckoutDate} " +
//            "AND o.reservationDateCheckoutDate BETWEEN :#{#searchDTO.reservationDateCheckinDate} AND :#{#searchDTO.reservationDateCheckoutDate}))")
//    List<RoomOrder> searchRoomOrder(SearchDTO searchDTO);


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
    
    
    
    

}

