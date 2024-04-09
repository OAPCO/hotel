package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {


    Optional<Manager> findByManagerId(String managerid);

    Optional<Manager> findByManagerIdx(Long managerIdx);

}
