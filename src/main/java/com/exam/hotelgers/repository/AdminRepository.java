package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {


    Optional<Admin> findByAdminId(String adminid);

    Optional<Admin> findByAdminIdx(Long adminIdx);

}
