package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Dist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistRepository extends JpaRepository<Dist, Long> {
    //사용자 아이디로 조회

    Optional<Dist> findByDistCd(String distCd);

    Optional<Dist> findByDistIdx(Long distIdx);


    Optional<Dist> findByDistName(String distName);

    @Query("SELECT d.distCd FROM Dist d")
    List<String> findAllDistCds();


}
