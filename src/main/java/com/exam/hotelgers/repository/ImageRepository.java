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


    
//    객실 세부 이미지 목록 조회
    @Query("select i from Image i where (i.roomIdx = :roomIdx and i.roomImageMain = 0)")
    List<Image> roomImageList (Long roomIdx);

    
    //객실 대표 이미지 조회
    @Query("select i from Image i where (i.roomIdx = :roomIdx and i.roomImageMain = 1)")
    Optional<Image> roomMainImage (Long roomIdx);



    //객실 상세 사진 보기
//    @Query(value = "select i from Image i join Room r on i.roomIdx = r.roomIdx where r.roomIdx = :roomIdx")
//    List<Image> roomDetailImageSearch2(Long roomIdx);


//    @Query(value = "select i from Image i join Room r on i.roomIdx = r.roomIdx " +
//            "join Store s on s.storeIdx = r.store.storeIdx " +
//            "where s.storeIdx = :storeIdx")
//    List<Image> roomDetailImageSearch2(Long StoreIdx);


//    @Query(value = "SELECT i.* FROM Image i JOIN Room r ON i.room_idx = r.room_idx JOIN Store s ON r.store_idx = s.store_idx", nativeQuery = true)
//    List<Image> roomDetailImageSearch2(Long StoreIdx);


    @Query(value = "SELECT i FROM Image i JOIN Room r ON i.roomIdx = r.roomIdx JOIN Store s ON r.store.storeIdx = s.storeIdx")
    List<Image> roomDetailImageSearch(Long StoreIdx);


    @Query(value = "SELECT i FROM Image i JOIN Room r ON i.roomIdx = r.roomIdx JOIN Store s ON r.store.storeIdx = s.storeIdx where i.roomImageMain = 1")
    List<Image> roomDetailMainImageSearch(Long StoreIdx);





}
