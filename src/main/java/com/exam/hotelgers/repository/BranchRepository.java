package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Branch;
import com.exam.hotelgers.entity.Dist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByBranchCd(String branchCd);

    Optional<Branch> findByBranchIdx(Long branchIdx);

    Optional<Branch> findByBranchName(String branchName);













}
