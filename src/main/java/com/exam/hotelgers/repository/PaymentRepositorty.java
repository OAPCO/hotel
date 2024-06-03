package com.exam.hotelgers.repository;


import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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


    //roomorderIdx로 payment 컬럼 조회
    @Query("select p from Payment p where p.roomorderIdx = :roomorderIdx")
    Optional<Payment> roomOrderByPaymentSearch(Long roomorderIdx);


    //distchiefId로 소유한 모든 결제 컬럼 가져옴
    @Query("select p from Payment p where p.distIdx IN (select d.distIdx from Dist d where d.distChief.distChiefId = :userid)")
    Page<Payment> distChiefPaymentSearch(Pageable pageable,String userid);



    //매장의 결제내역 리스트
    @Query("select p from Payment p where p.storeIdx = :#{#searchDTO.storeIdx} " +
            "and ((:#{#searchDTO.startLocalDate} is null) or (p.regdate BETWEEN :#{#searchDTO.startLocalDate} AND :#{#searchDTO.endLocalDate})) " +
            "and ((:#{#searchDTO.paymentStatus} is null) or (p.paymentStatus = :#{#searchDTO.paymentStatus}))")
    Page<Payment> storesalesDateSearch(Pageable pageable, SearchDTO searchDTO);



    //내 매장의 매출 확인 쿼리들
    //연도별
    @Query("SELECT YEAR(p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "and p.paymentStatus = 0 " +
            "GROUP BY YEAR(p.regdate) " +
            "ORDER BY YEAR(p.regdate) ")
    List<Object[]> getYearSales(Long storeIdx);

    @Query("SELECT MONTH(p.regdate)," +
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "and p.paymentStatus = 0 " +
            "GROUP BY MONTH (p.regdate) " +
            "ORDER BY month (p.regdate)")
    List<Object[]> getMonthSales(Long storeIdx);


    //일별
    @Query("SELECT DAY(p.regdate),"+
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.storeIdx = :storeIdx " +
            "and p.paymentStatus = 0 " +
            "GROUP BY day (p.regdate)" +
            "ORDER BY day (p.regdate)")
    List<Object[]> getDaySales(Long storeIdx);









    //내 총판의 매출 확인 쿼리들
    //연도별
    @Query("SELECT YEAR(p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p " +
            "WHERE p.distIdx = :distIdx " +
            "and (p.paymentStatus = 0 or p.paymentStatus = 2) " +
            "GROUP BY YEAR(p.regdate) " +
            "ORDER BY YEAR(p.regdate) ")
    List<Object[]> getDistYearSales(Long distIdx);

    @Query("SELECT MONTH(p.regdate)," +
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.distIdx = :distIdx " +
            "and (p.paymentStatus = 0 or p.paymentStatus = 2) " +
            "GROUP BY MONTH (p.regdate) " +
            "ORDER BY month (p.regdate)")
    List<Object[]> getDistMonthSales(Long distIdx);


    //일별
    @Query("SELECT DAY(p.regdate),"+
            "SUM(p.paymentPrice) " +
            "FROM Payment p " +
            "WHERE p.distIdx = :distIdx " +
            "and (p.paymentStatus = 0 or p.paymentStatus = 2) " +
            "GROUP BY day (p.regdate)" +
            "ORDER BY day (p.regdate)")
    List<Object[]> getDistDaySales(Long distIdx);











    //내 전체 총판의 매출 확인 쿼리들
    //연도별
    @Query("SELECT YEAR(p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p left join Dist d on d.distIdx = p.distIdx left join DistChief c on d.distChief.distChiefIdx = c.distChiefIdx " +
            "WHERE c.distChiefIdx = :distChiefIdx " +
            "and (p.paymentStatus = 0 or p.paymentStatus = 2) " +
            "GROUP BY YEAR(p.regdate) " +
            "ORDER BY YEAR(p.regdate) ")
    List<Object[]> getDistChiefYearSales(Long distChiefIdx);


    //내 전체 총판의 매출 확인 쿼리들
    //연도별
    @Query("SELECT MONTH (p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p left join Dist d on d.distIdx = p.distIdx left join DistChief c on d.distChief.distChiefIdx = c.distChiefIdx " +
            "WHERE c.distChiefIdx = :distChiefIdx " +
            "and (p.paymentStatus = 0 or p.paymentStatus = 2) " +
            "GROUP BY month (p.regdate) " +
            "ORDER BY month (p.regdate) ")
    List<Object[]> getDistChiefMonthSales(Long distChiefIdx);


    //내 전체 총판의 매출 확인 쿼리들
    //연도별
    @Query("SELECT DAY (p.regdate) ," +
            "SUM(p.paymentPrice)  " +
            "FROM Payment p left join Dist d on d.distIdx = p.distIdx left join DistChief c on d.distChief.distChiefIdx = c.distChiefIdx " +
            "WHERE c.distChiefIdx = :distChiefIdx " +
            "and (p.paymentStatus = 0 or p.paymentStatus = 2) " +
            "GROUP BY day (p.regdate) " +
            "ORDER BY day (p.regdate) ")
    List<Object[]> getDistChiefDaySales(Long distChiefIdx);



    @Modifying
    @Query("update Payment p set " +
            "p.paymentStatus = 1 " +
            "where p.roomorderIdx = :roomorderIdx")
    void paymentCancel(Long roomorderIdx);

    @Modifying
    @Query("update Payment p set " +
            "p.paymentStatus = 2 " +
            "where p.roomorderIdx = :roomorderIdx")
    void paymentCancelCharge(Long roomorderIdx);
    

}

