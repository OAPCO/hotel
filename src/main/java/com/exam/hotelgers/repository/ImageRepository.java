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


    //객실타입,스토어idx로 객실 디테일 이미지 목록 조회
    @Query("select i from Image i join Room r on i.roomImageType = r.roomType where r.store.storeIdx = :storeIdx and i.roomImageType = :roomType")
    List<Image> roomDetailImageList (String roomType, Long storeIdx);



    //호텔의 모든 객실 상세 이미지 목록
    @Query(value = "SELECT i FROM Image i JOIN Room r ON i.roomImageType LIKE r.roomType JOIN Store s ON r.store.storeIdx = s.storeIdx")
    List<Image> roomDetailImageSearch(Long StoreIdx);





    //호텔 객실 타입의 이미지 목록
    @Query(value = "SELECT i FROM Image i JOIN Room r ON i.roomImageType LIKE r.roomType JOIN Store s ON r.store.storeIdx = s.storeIdx " +
            "where i.roomImageMain = 0 and " +
            "i.roomImageType LIKE :#{#searchDTO.roomType} and " +
            "r.store.storeIdx = :#{#searchDTO.storeIdx}")
    List<Image> roomTypeDetailMainImageSearch(SearchDTO searchDTO);



    //호텔 객실 타입의 대표이미지
    @Query(value = "SELECT i FROM Image i JOIN Room r ON i.roomImageType = r.roomType JOIN Store s ON r.store.storeIdx = s.storeIdx " +
            "where i.roomImageMain = 1 and " +
            "i.roomImageType LIKE :#{#searchDTO.roomType} and " +
            "r.store.storeIdx = :#{#searchDTO.storeIdx}")
    Optional<Image> roomTypeMainImageSearch(SearchDTO searchDTO);



    //호텔의 모든 객실 대표 이미지 목록
    @Query(value = "SELECT i FROM Image i JOIN Room r ON i.roomImageType = r.roomType JOIN Store s ON r.store.storeIdx = s.storeIdx where i.roomImageMain = 1")
    List<Image> roomDetailMainImageSearch(Long StoreIdx);


//    //호텔의 비어있는 객실의 대표 이미지 목록을 만드는 쿼리
//    @Query(value = "SELECT i FROM Image i JOIN Room r ON i.roomImageType = r.roomType JOIN Store s ON r.store.storeIdx = s.storeIdx where i.roomImageType like %:roomType%")
//    List<Image> emptyRoomDetailMainImageSearch(List<String> roomType);




    //객실타입으로 검색해서 기존에 있는 이미지로 등록을 하기
    //storeidx와 객실타입이 일치하는 행들중 main이미지 컬럼이 1인 행을 찾아서








}
