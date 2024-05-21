package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Image;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.RoomOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{



    Optional<Room> findByRoomCd(String roomCd);
    Optional<Room> findByRoomType(String roomType);

    Optional<Room> findByRoomIdx(Long roomIdx);


    @Query("select r from Room r join r.store s where (r.store.storeCd = :storeCd)")
    List<Room> loginManagerRoomSearch(String storeCd);


    //호텔 예약 빈 방 체크 쿼리
    //room테이블,roomorder테이블 조인 후 roomstatus가 0(빈 방)이거나
    //1(예약됨),2(사용중)인데 뷰에서 받은 체크인 희망 날짜가 roomorder의 체크아웃 날짜보다 크다면 예약을 받을 수 있기 때문에 조회한다.
    @Query(value = "SELECT r FROM Room r " +
            "LEFT JOIN RoomOrder o ON r.store.storeIdx = o.storeIdx " +
            "WHERE r.store.storeIdx = :#{#searchDTO.storeIdx} " +
            "AND (r.roomStatus = 0 OR (r.roomStatus IN (1,2,3) AND o.reservationDateCheckout < :#{#searchDTO.reservationDateCheckin})) " +
            "GROUP BY r.roomType")
    List<Room> searchEmptyRoom(SearchDTO searchDTO);
    

    //호텔의 모든 객실 종류 중복없이 출력
    @Query("SELECT r1 FROM Room r1 WHERE r1.roomIdx IN " +
            "(SELECT MIN(r2.roomIdx) FROM Room r2 GROUP BY r2.roomType) " +
            "and r1.store.storeIdx = :storeIdx")
    List<Room> roomTypeSearch(Long storeIdx);


    //호텔의 특정 roomType의 정보 불러오는 쿼리 (사용처 : 매니저의 객실 생성 페이지 - roomregister)
    @Query("SELECT r1 FROM Room r1 WHERE r1.roomIdx IN " +
            "(SELECT MIN(r2.roomIdx) FROM Room r2 GROUP BY r2.roomType) " +
            "and r1.store.storeIdx = :storeIdx " +
            "and r1.roomType = :roomType")
    Optional<Room> roomTypeSearchOne(Long storeIdx,String roomType);



    //예약불가 객실 찾는 쿼리
    @Query("SELECT r1 FROM Room r1 WHERE r1.roomIdx IN " +
            "(SELECT MIN(r2.roomIdx) FROM Room r2 GROUP BY r2.roomType) " +
            "and r1.store.storeIdx = :storeIdx " +
            "and r1.roomType = :roomType")
    Optional<Room> notEmptyRoomTypeSearch(Long storeIdx, String roomType);
    
    


    //객실 상태 변경
    @Modifying
    @Query("update Room r set " +
            "r.roomStatus = :#{#roomOrderDTO.roomStatus} " +
            "where r.roomIdx = :#{#roomOrderDTO.roomIdx}")
    void roomStatusUpdate(RoomOrderDTO roomOrderDTO);


    //예약을 받았을 때 객실 하나의 status를 1로 바꿔야하기때문에 필요한 쿼리.
    @Modifying
    @Query("update Room r set " +
            "r.roomStatus = 1 " +
            "where r.roomIdx = :#{#roomOrderDTO.roomIdx}")
    void roomStatusUpdate1(RoomOrderDTO roomOrderDTO);






    //해당 매장의 roomCd로 roomIdx를 구한다. (다음 쿼리를 위한 물밑작업)
    @Query(value = "SELECT r.roomIdx FROM Room r WHERE r.roomCd = :roomCd and r.store.storeIdx = :storeIdx")
    Long searchRoomIdx(String roomCd,Long storeIdx);



    //체크인 했을 때 객실 상태를 2로 변경한다.
    @Modifying
    @Query("UPDATE Room r " +
            "SET r.roomStatus = 2 " +
            "WHERE r.roomIdx = :roomIdx " +
            "AND EXISTS (SELECT 1 FROM RoomOrder o WHERE o.roomIdx = r.roomIdx AND o.roomorderIdx = :roomorderIdx)")
    void roomStatusUpdate2(Long roomIdx, Long roomorderIdx);


    //기존 객실타입으로 객실을 추가할 때 메인이미지파일명이 저장되지 않으므로 별도의 쿼리문을 추가한다.
    @Query(value = "SELECT room_mainimg_name FROM Room r WHERE room_type = :roomType and store_idx = :storeIdx LIMIT 1", nativeQuery = true)
    String roomTypeMainImgSearch(String roomType,Long storeIdx);



}
