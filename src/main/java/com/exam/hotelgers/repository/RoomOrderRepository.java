package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Qna;
import com.exam.hotelgers.entity.RoomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomOrderRepository extends JpaRepository<RoomOrder,Long> {
    List<RoomOrder>findByStoreIdx(Long storeIdx);


}
