package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.StoreBranch;
import com.exam.hotelgers.entity.StoreDist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreBranchRepository extends JpaRepository<StoreBranch, Long> {

    Optional<StoreBranch> findByStoreBranchId(String storeBranchId);

    Optional<StoreBranch> findByStoreBranchIdx(Long storeBranchIdx);






    List<StoreBranch> findByStoreDistStoreDistIdx(StoreDist storeDist);








}
