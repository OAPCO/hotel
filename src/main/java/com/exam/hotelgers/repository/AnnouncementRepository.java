package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    Optional<Announcement> findByNoticeIdx(String title);
}
