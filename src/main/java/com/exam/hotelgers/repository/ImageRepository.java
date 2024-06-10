package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {



    //배너 이미지 목록 조회
    @Query("select i from Image i where (i.bannerIdx = :bannerIdx)")
    List<Image> bannerImageList (Long bannerIdx);


    //호텔의 모든 객실 상세 이미지 목록
    @Query(value = "SELECT distinct i.* FROM Image i JOIN Room r ON i.room_image_type = r.room_type JOIN Store s ON i.store_idx = r.store_idx where i.store_idx = 2",nativeQuery = true)
    List<Image> roomDetailImageSearch(Long StoreIdx);



    //호텔의 모든 객실 대표 이미지 목록
    @Query(value = "SELECT i FROM Image i JOIN Room r " +
            "ON i.roomImageType = r.roomType " +
            "JOIN Store s ON r.store.storeIdx = s.storeIdx " +
            "where i.roomImageMain = 1")
    List<Image> roomDetailMainImageSearch(Long StoreIdx);




    //호텔 객실 타입의 이미지 목록
    @Query(value = "SELECT i FROM Image i " +
            "where i.roomImageType LIKE :#{#searchDTO.roomType} and " +
            "i.storeIdx = :#{#searchDTO.storeIdx}")
    List<Image> roomTypeDetailMainImageSearch(SearchDTO searchDTO);



    //호텔 객실 타입의 대표이미지
    @Query(value = "SELECT i FROM Image i " +
            "where i.roomImageMain = 1 and " +
            "i.roomImageType = :#{#searchDTO.roomType} and " +
            "i.storeIdx = :#{#searchDTO.storeIdx}")
    Optional<Image> roomTypeMainImageSearch(SearchDTO searchDTO);














}
