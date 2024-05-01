package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
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


    Optional<DistChief> findByDistChiefId(String distChiefid);

    Optional<DistChief> findByDistChiefName(String distChiefName);

    @Query("select d from DistChief d where (:#{#searchDTO.distChiefName} is null or d.distChiefName LIKE %:#{#searchDTO.distChiefName}%)")
    Page<DistChief> distChiefSearch(SearchDTO searchDTO,Pageable pageable);

}
