package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Image;
import com.exam.hotelgers.entity.Room;
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
    //1(예약됨),2(사용중)인데 뷰에서 받은 체크인 희망 날짜가 roomorder의 체크아웃 날짜보다 크다면 예약을 받을 수 있기 때문에 조회한다.
    @Query("select r from Room r join RoomOrder o on r.roomIdx = o.roomIdx " +
            "where r.roomStatus = 0 " +
            "or ((r.roomStatus IN (1,2)) and (o.reservationDateCheckout < :#{#searchDTO.reservationDateCheckin}))")
    List<Room> searchEmptyRoom(SearchDTO searchDTO);


    //객실 종류 출력 (서브쿼리 이용)
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




}
