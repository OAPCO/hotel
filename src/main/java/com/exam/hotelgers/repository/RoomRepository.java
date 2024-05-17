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

    Optional<Room> findByRoomIdx(Long roomIdx);


    @Query("select r from Room r join r.store s where (r.store.storeCd LIKE %:storeCd%)")
    List<Room> loginManagerRoomSearch(String storeCd);


    //호텔 예약 빈 방 체크 쿼리
    //room테이블,roomorder테이블 조인 후 roomstatus가 0(빈 방)이거나
    //1(예약됨),2(사용중)인데 뷰에서 받은 체크인 희망 날짜가 roomorde    r의 체크아웃 날짜보다 크다면 예약을 받을 수 있기 때문에 조회한다.
//    @Query("select r from Room r " +
//            "left join RoomOrder o on r.store.storeIdx = o.storeIdx and " +
//            "(r.roomStatus = 0 or (r.roomStatus IN (1,2) and o.reservationDateCheckout < :#{#searchDTO.reservationDateCheckin})) " +
//            "where r.store.storeIdx = :#{#searchDTO.storeIdx} " +
//            "group by r.roomType")

    @Query(value = "SELECT r FROM Room r " +
            "LEFT JOIN RoomOrder o ON r.store.storeIdx = o.storeIdx " +
            "WHERE r.store.storeIdx = :#{#searchDTO.storeIdx} " +
            "AND (r.roomStatus = 0 OR (r.roomStatus IN (1,2) AND o.reservationDateCheckout < :#{#searchDTO.reservationDateCheckin})) " +
            "GROUP BY r.roomType")
    List<Room> searchEmptyRoom(SearchDTO searchDTO);


//    SELECT r.*
//    FROM Room r
//    LEFT JOIN RoomOrder o ON r.store_Idx = o.store_Idx
//    WHERE r.store_Idx = :storeId
//    AND (r.room_Status = 0 OR (r.room_Status IN (1,2) AND o.reservation_date_checkout < :reservationDateCheckin))
//    GROUP BY r.room_Type;




    //호텔의 모든 객실 종류 중복없이 출력
    @Query("SELECT r1 FROM Room r1 WHERE r1.roomIdx IN " +
            "(SELECT MIN(r2.roomIdx) FROM Room r2 GROUP BY r2.roomType) " +
            "and r1.store.storeIdx = :storeIdx")
    List<Room> roomTypeSearch(Long storeIdx);


    //객실 상태 변경
    @Modifying
    @Query("update Room r set " +
            "r.roomStatus = :#{#roomOrderDTO.roomStatus} " +
            "where r.roomIdx = :#{#roomOrderDTO.roomIdx}")
    void roomStatusUpdate(RoomOrderDTO roomOrderDTO);


    //예약을 받았을 때 객실 하나의 status를 3으로 바꿔야하기때문에 필요한 쿼리.
    @Modifying
    @Query("update Room r set " +
            "r.roomStatus = 3 " +
            "where r.roomIdx = :#{#roomOrderDTO.roomIdx}")
    void roomStatusUpdate3(RoomOrderDTO roomOrderDTO);





}
