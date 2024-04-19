package com.exam.hotelgers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchChiefRepository extends JpaRepository<BranchChief,Long> {


    Optional<BranchChief> findByBranchChiefId(String branchChiefid);

    Optional<BranchChief> findByBranchChiefIdx(Long branchChiefIdx);

}
