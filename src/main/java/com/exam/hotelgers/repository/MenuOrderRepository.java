package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.MenuOrder;

import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.RoomOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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


    @Query("select m from MenuOrder m where m.menuorderCd = :menuOrderCd")
    Optional<MenuOrder> findMenuSheetPayment(String menuOrderCd);

    List<MenuOrder>findByMemberIdx(Long memberIdx);

    Page<MenuOrder> findByStoreIdx(Long storeIdx, Pageable pageable);

    List<MenuOrder> findByStoreIdx(Long storeIdx);


    List<MenuOrder> findByRoomorderIdx(Long roomIdx);





}