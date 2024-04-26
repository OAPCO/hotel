package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.MenuSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MenuSheetRepository extends JpaRepository<MenuSheet,Long> {
    Optional<MenuSheet> findByNewOrderNo(Integer newOrderNo);

    Optional<MenuSheet> findByMenuSheetIdx(Long menuSheetIdx);
    
    @Query("select o from MenuSheet o where (:storeName is null or o.store.storeName LIKE %:storeName%)"+
            "and (:roomCd is null or o.room.roomCd LIKE %:roomCd%)"+
            "and (:newOrderNo is null or o.newOrderNo = %:newOrderNo%)"+
            "and (:menuSheetState is null or o.menuSheetState = %:menuSheetState%)"+
            "and (:startDate is null or o.orderdate >= %:startDate%)"+
            "and (:endDate is null or o.orderdate <= %:endDate%)"+
            "and (:orderProgressStatus is null or o.orderProgressStatus LIKE %:orderProgressStatus%)"+
            "and (:menuSheetName is null or o.menuSheetName LIKE %:menuSheetName%)"
    )
    Page<MenuSheet> menuSheetListSearch(@Param("storeName") String storeName,//매장명
                                    @Param("roomCd") String roomCd,//룸 코드
                                    @Param("newOrderNo") Integer newOrderNo,//신규 주문번호
                                    @Param("menuSheetState") Integer menuSheetState,//주문서 상태 0.주문전, 1.조리요청, 2.결제요청, 3.결제완료, 4.결제취소, 5.조리완료, 6.배달요청, 7.배달완료
                                    @Param("startDate") LocalDateTime startDate,//시작날짜
                                    @Param("endDate") LocalDateTime endDate,//종료날짜
                                    @Param("orderProgressStatus") String orderProgressStatus,//주문상태(NEW 신규,CHECK 접수,CANCEL 취소,CALL 호출,CLOSE 완료)
                                    @Param("menuSheetName") String menuSheetName,//주문서 이름
                                    Pageable pageable);
}
