package com.exam.hotelgers.repository;

import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{



    Optional<Room> findByRoomCd(String roomCd);

    Optional<Room> findByRoomIdx(Long roomIdx);


    @Query("select r from Room r join r.store s where (r.store.storeCd LIKE %:storeCd%)")
    Page<Room> loginManagerRoomSearch(String storeCd,Pageable pageable);

    @Query("select b from Brand b join b.storeList s where (:distName is null or b.dist.distName LIKE %:distName%)"+
            "AND (:brandName IS NULL OR b.brandName LIKE %:brandName%) " +
            "AND (:brandCd IS NULL OR b.brandCd LIKE %:brandCd%)"
    )
    Page<Brand> multisearch2(
            @Param("distName") String distName,
            @Param("brandName") String brandName,
            @Param("brandCd") String brandCd,
            Pageable pageable
    );

    





}
