package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.SalesMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SalesMonthRepository extends JpaRepository<SalesMonth,Long> {
    @Query("SELECT u FROM SalesMonth u WHERE "+
            "(:startDate IS NULL OR u.date >=:startDate) "+
            "AND (:endDate IS NULL OR u.date <=:endDate) "+
            "AND (:distributor_organization IS NULL OR u.distributor_organization LIKE %:distributor_organization%)" +
            "AND (:Branch IS NULL OR u.Branch LIKE %:Branch%)" +
            "AND (:store IS NULL OR u.store LIKE %:store%)")
            //상품이름이 null이 아니면 상품이름이 포함된 자료를 조회
            //처음일이 null이 아니면 처음일의 해당날짜와 일치하거나 크면 조회
            //종료일이 null이 아니면 종료일의 해당날짜와 일치하거나 작으면 조회
    Page<SalesMonth> search(@Param("distributor_organization") String distributor_organization, //총판조직
                       @Param("Branch") String Branch, //지사
                       @Param("store") String store, //매장
                       @Param("startDate") LocalDate startDate, //처음일
                       @Param("endDate") LocalDate endDate, //종료일
                       Pageable pageable);
}
