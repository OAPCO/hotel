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


    
//    룸 이미지 목록 조회
    @Query("select i from Image i where (i.roomIdx = :roomIdx)")
    List<Image> roomImageList (Long roomIdx);



    //객실 상세 사진 보기
    @Query(value = "select i from Image i join Room r on i.roomIdx = r.roomIdx where r.roomIdx = :roomIdx")
    List<Image> roomDetailImageSearch(Long roomIdx);


    @Query(value = "select i from Image i join Room r on i.roomIdx = r.roomIdx " +
            "join Store s on s.storeIdx = r.store.storeIdx " +
            "where s.storeIdx = :storeIdx")
    List<Image> roomDetailImageSearch2(Long StoreIdx);





}
