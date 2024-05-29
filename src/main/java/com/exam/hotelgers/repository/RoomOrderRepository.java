package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomOrderRepository extends JpaRepository<RoomOrder,Long> {


    List<RoomOrder> findByStoreIdx(Long storeIdx);


    //roomorderIdx로 storeIdx 찾기(체크인 후 룸서비스 주문할 때 메뉴선택에 사용하기 위해)
    @Query("select o.storeIdx from RoomOrder o where o.roomorderIdx = :roomorderIdx")
    Long findStoreIdx(Long roomorderIdx);


    @Query("select r from RoomOrder r where r.roomIdx = :roomorderIdx " +
            "and r.roomStatus = 1")
    Optional<RoomOrder> roomOrderIspresentCheck(Long roomorderIdx);


    //현재 예약 요청들
    @Query("SELECT r FROM RoomOrder r where r.storeIdx = :storeIdx " +
            "and r.roomStatus = 1")
    Page<RoomOrder> roomOrderSearch(Pageable pageable,Long storeIdx);

    @Query("SELECT r FROM RoomOrder r where r.storeIdx = :storeIdx " +
            "and r.roomStatus = 1")
    List<RoomOrder> roomOrderSearch2(Long storeIdx);


    //room의 지난 내역(roomstatus=4)들 확인
    @Query("select r from RoomOrder r where r.roomIdx = :#{#searchDTO.roomIdx} " +
            "and r.roomStatus = 4")
    Page<RoomOrder> endRoomOrderSearch(Pageable pageable,SearchDTO searchDTO);


//    room의 예약 현황,지난 내역들 정보 (room/read에서 사용)
    @Query("select r from RoomOrder r where r.roomIdx = :#{#searchDTO.roomIdx} " +
            "and r.roomStatus IN (1,4) ORDER BY r.roomStatus")
    List<RoomOrder> RoomOrderAllSearch(SearchDTO searchDTO);


    //room의 현재 예약 정보 확인
    @Query("select r from RoomOrder r where r.roomIdx = :#{#searchDTO.roomIdx} " +
            "and r.roomStatus = 1")
    Optional<RoomOrder> roomOrderStatusCheck(SearchDTO searchDTO);



    //특정 객실의 현재 사용중인 객실주문 가져오기
    @Query("SELECT o FROM RoomOrder o where o.roomIdx = :roomIdx and o.roomStatus = 2")
    Optional<RoomOrder> roomOrder2Check(Long roomIdx);




    Optional<RoomOrder> findByMemberIdx(Long memberIdx);

    List<RoomOrder> findAllByMemberIdx(Long memberIdx);

    Page<RoomOrder> findByStoreIdx(Long storeIdx, Pageable pageable);



    //체크인 했을 때 객실 상태를 2로 변경한다.
    @Modifying
    @Query("UPDATE RoomOrder o " +
            "SET o.roomStatus = 2, o.checkinTime = :checkinTime " +
            "WHERE o.roomIdx = :roomIdx " +
            "AND EXISTS (SELECT 1 FROM Room r WHERE o.roomIdx = r.roomIdx AND o.roomorderIdx = :roomorderIdx)")
    void roomOrderStatusUpdate2(Long roomIdx, Long roomorderIdx, LocalDateTime checkinTime);



//    끼인 룸오더 찾기
    @Query(value = "SELECT o FROM RoomOrder o " +
            "LEFT JOIN Room r ON r.store.storeIdx = o.storeIdx " +
            "WHERE o.storeIdx = :#{#searchDTO.storeIdx} " +
            "and o.roomOrderType = :#{#searchDTO.roomType} " +
            "AND (:#{#searchDTO.reservationDateCheckinDate} BETWEEN o.reservationDateCheckinDate AND o.reservationDateCheckoutDate " +
            "OR :#{#searchDTO.reservationDateCheckoutDate} BETWEEN o.reservationDateCheckinDate AND o.reservationDateCheckoutDate " +
            "OR (o.reservationDateCheckinDate BETWEEN :#{#searchDTO.reservationDateCheckinDate} AND :#{#searchDTO.reservationDateCheckoutDate} " +
            "AND o.reservationDateCheckoutDate BETWEEN :#{#searchDTO.reservationDateCheckinDate} AND :#{#searchDTO.reservationDateCheckoutDate}))")
    List<RoomOrder> searchRoomOrder(SearchDTO searchDTO);


    //회원이 현재 묵고 있는 방 찾는 쿼리
    @Query("select r from RoomOrder r where r.memberIdx = :memberIdx " +
            "and r.roomStatus = 2")
    Optional<RoomOrder> findmemberInRoomOrder(Long memberIdx);

    //오늘 날짜가 룸오더의 예상 체크인 체크아웃 날짜 사이에 위치하는 것만 memberIdx로 검색해서 가져오기
    @Query("SELECT ro FROM RoomOrder ro WHERE ro.memberIdx = :memberIdx AND :today BETWEEN ro.reservationDateCheckinDate AND ro.reservationDateCheckoutDate")
    RoomOrder findRoomOrderForToday(@Param("memberIdx") Long memberIdx, @Param("today") LocalDateTime today);


    //객실 예약 취소시 roomorderIdx로 조회해서 삭제한다
    @Modifying
    @Query("delete RoomOrder r where r.roomorderIdx = :roomorderIdx")
    void roomOrderDelete(Long roomorderIdx);


}
