package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Qna;
import com.exam.hotelgers.entity.RoomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomOrderRepository extends JpaRepository<RoomOrder,Long> {


}
