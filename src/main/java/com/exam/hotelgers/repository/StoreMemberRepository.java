package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.StoreMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMemberRepository extends JpaRepository<StoreMember, Long> {
}
