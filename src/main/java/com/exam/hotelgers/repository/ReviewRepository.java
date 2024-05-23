package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Review;
import com.exam.hotelgers.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.store = :store")
    Double getAverageRatingForStore(Store store);
}