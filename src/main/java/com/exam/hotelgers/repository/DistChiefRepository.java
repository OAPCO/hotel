package com.exam.hotelgers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistChiefRepository extends JpaRepository<DistChief,Long> {


    Optional<DistChief> findByDistChiefId(String distChiefid);

    Optional<DistChief> findByDistChiefIdx(Long distChiefIdx);

}
