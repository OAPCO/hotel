package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    @Query("select m from Manager m where m.managerId LIKE %:#{#searchDTO.managerId}%")
    Optional<Manager> managerCheckGet(SearchDTO searchDTO);


    Optional<Manager> findByManagerId(String managerid);

    Optional<Manager> findByManagerIdx(Long managerIdx);

    Optional<Manager> findByManagerName(String managerName);


    @Query("select m from Manager m where (:#{#searchDTO.distName} is null or m.dist.distName LIKE %:#{#searchDTO.distName}%)")
    List<Manager> distOfManager(SearchDTO searchDTO);


    @Query("select m.managerId from Manager m where (m.managerId LIKE %:userid%)")
    List<String> registerCheck (String userid);




}
