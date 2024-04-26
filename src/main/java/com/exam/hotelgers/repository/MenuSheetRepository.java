package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.MenuSheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuSheetRepository extends JpaRepository<MenuSheet,Long> {
    Optional<MenuSheet> findByNewOrderNo(Integer newOrderNo);

    Optional<MenuSheet> findByMenuSheetIdx(Long menuSheetIdx);
    
    @Query("select o from MenuSheet o where (:storeName is null or o.store.storeName LIKE %:storeName%)"+
            "and (:roomCd is null or o.room.roomCd LIKE %:roomCd%)"+
            "and (:newOrderNo is null or o.newOrderNo = %:newOrderNo%)"+
            "and (:menuSheetState is null or o.menuSheetState = %:menuSheetState%)"

    )
    Page<MenuSheet> orderListSearch(@Param("storeName") String storeName,
                                    @Param("roomCd") String roomCd,
                                    @Param("newOrderNo") String newOrderNo,
                                    @Param("menuSheetState") String menuSheetState,
                                    Pageable pageable);
}
