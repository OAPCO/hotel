package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistChiefRepository extends JpaRepository<DistChief,Long> {


    //중복 아이디 체크용
    @Query("select d.distChiefId from DistChief d where (d.distChiefId LIKE %:userid%)")
    List<String> registerCheck (String userid);


    Optional<DistChief> findByDistChiefId(String distChiefid);


    @Query("select d from DistChief d where d.distChiefId = :userId")
    Optional<DistChief> distChiefSearchforUserId(String userId);



}
