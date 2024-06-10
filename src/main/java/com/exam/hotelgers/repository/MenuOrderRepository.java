package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.MenuOrder;

import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.RoomOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuOrderRepository extends JpaRepository<MenuOrder,Long> {



    Optional<MenuOrder> findByMenuorderIdx(Long menuorderIdx);

    @Query("select m from MenuOrder m where m.roomorderIdx = :roomorderIdx")
    List<MenuOrder> findByListMenuorderIdx(Long roomorderIdx);



    List<MenuOrder>findByMemberIdx(Long memberIdx);



    @Query("select m from MenuOrder m where m.storeIdx = :storeIdx order by m.menuorderIdx desc")
    List<MenuOrder> findByStoreIdxMenuOrder(Long storeIdx);


    List<MenuOrder> findByRoomorderIdx(Long roomIdx);



    //매니저가 원하는대로 menuorder state 변경
    @Modifying
    @Query("UPDATE MenuOrder o " +
            "SET o.orderState = :orderStatus " +
            "WHERE o.menuorderIdx = :menuorderIdx")
    void menuOrderStatusChange(Long menuorderIdx, String orderStatus);
    
    
    //menuorder state 3(결제대기)->0(결제완료) 로 변경
    @Modifying
    @Query("UPDATE MenuOrder o " +
            "SET o.orderState = '0' " +
            "WHERE o.menuorderIdx = :menuorderIdx")
    void menuOrderPaymentCheck(Long menuorderIdx);
    
    
    //meorderCd로 menuorderIdx 찾기
    @Query("select m.menuorderIdx from MenuOrder m where m.menuorderCd = :menuorderCd")
    Long findMenuorderIdx(String menuorderCd);



}