package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.MenuOrder;

import com.exam.hotelgers.entity.RoomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuOrderRepository extends JpaRepository<MenuOrder,Long> {



    Optional<MenuOrder> findByMenuorderIdx(Long menuorderIdx);
//    List<MenuOrder> MenuOrderfindByMemberIdx(Long memberIdx);
}