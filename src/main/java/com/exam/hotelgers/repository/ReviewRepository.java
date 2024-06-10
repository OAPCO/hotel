package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Review;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.store.storeIdx = :storeIdx")
    List<Review> findByStoreIdx(@Param("storeIdx") Long storeIdx);
}