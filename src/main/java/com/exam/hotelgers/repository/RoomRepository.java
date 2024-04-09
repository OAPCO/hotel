package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByStoreId(Long storeId, Pageable pageable);
}
