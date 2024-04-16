package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SalesRepository extends JpaRepository<Sales,Long> {
    @Query("SELECT u FROM Sales u WHERE (:distributor_organization IS NULL OR u.distributor_organization LIKE %:distributor_organization%) " +
            "AND (:Branch IS NULL OR u.Branch LIKE %:Branch%) " +
            "AND (:store IS NULL OR u.store LIKE %:store%) "+
            "AND (:storename IS NULL OR u.storename LIKE %:storename%) "+
            "AND (:date IS NULL OR u.date = :date) "+
            "AND (:payment_method IS NULL OR u.payment_method LIKE %:payment_method%) "+
            "AND (:processing_status IS NULL OR u.processing_status LIKE %:processing_status%) ")
    Page<Sales> search(@Param("storename") String storename, //매장명
                       @Param("payment_method") String  payment_method, //결제방식
                       @Param("processing_status") String processing_status, //처리상태
                       @Param("date") LocalDate date, //일자
                       @Param("distributor_organization") String distributor_organization, //총판조직
                       @Param("Branch") String Branchx, //지사
                       @Param("store") String store,
                       Pageable pageable); //매장
    @Query("SELECT e FROM Sales e WHERE " +
            "(:type = '1m' and e.moddate >= :oneMonthAgo or " +
            "(:type = '3m' and e.moddate >= :threeMonthAgo) or " +
            "(:type = '6m' and e.moddate >= :sixMonthsAgo) or " +
            "(:type = '1y' and e.moddate >= :oneYearsAgo))")
    Page<Sales> findByType(@Param("oneMonthAgo") LocalDateTime oneMonthAgo,
                                 @Param("threeMonthAgo") LocalDateTime threeMonthAgo,
                                 @Param("sixMonthsAgo") LocalDateTime sixMonthsAgo,
                                 @Param("oneYearsAgo") LocalDateTime oneYearsAgo,
                                 Pageable pageable);
    Page<Sales> findByModdateBetween(LocalDateTime startDate,
                                           LocalDateTime endDate, Pageable pageable);

    @Query("SELECT e FROM Sales e WHERE " +
            "(e.startDate <= :startDate and e.endDate > :startDate) "+ //시작날짜가 기존 예약일 안에 포함되는가
            "or (e.startDate <= :endDate and e.endDate > :endDate) "  //끝나는 날짜가 기존 예약일 안에 포함되는가
    )
    Optional<Sales> revDate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
