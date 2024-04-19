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
    @Query("SELECT u FROM Sales u WHERE "+
            "(:storename IS NULL OR u.storename LIKE %:storename%) "+
            "AND (:payment_method IS NULL OR u.payment_method LIKE %:payment_method%) "+
            "AND (:processing_status IS NULL OR u.processing_status LIKE %:processing_status%) "+
            "AND (:startDate IS NULL OR u.date >=:startDate)" +
            "AND (:endDate IS NULL OR u.date <=:endDate)" +
            "AND (:distributor_organization IS NULL OR u.distributor_organization LIKE %:distributor_organization%)" +
            "AND (:Branch IS NULL OR u.Branch LIKE %:Branch%)" +
            "AND (:store IS NULL OR u.store LIKE %:store%)")
            //상품이름이 null이 아니면 상품이름이 포함된 자료를 조회
            //처음일이 null이 아니면 처음일의 해당날짜와 일치하거나 크면 조회
            //종료일이 null이 아니면 종료일의 해당날짜와 일치하거나 작으면 조회
    Page<Sales> search(@Param("storename") String storename, //매장명
                       @Param("payment_method") String  payment_method, //결제방식
                       @Param("processing_status") String processing_status, //처리상태
                       @Param("distributor_organization") String distributor_organization, //총판조직
                       @Param("Branch") String Branch, //지사
                       @Param("store") String store, //매장
                       @Param("startDate") LocalDate startDate, //처음일
                       @Param("endDate") LocalDate endDate, //종료일
                       Pageable pageable);
}
