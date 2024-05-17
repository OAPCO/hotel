package com.exam.hotelgers.repository;

import com.exam.hotelgers.dto.RoomOrderDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.Qna;
import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.RoomOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomOrderRepository extends JpaRepository<RoomOrder,Long> {


    List<RoomOrder> findByStoreIdx(Long storeIdx);


    @Query("select r from RoomOrder r where r.roomIdx = :roomorderIdx " +
            "and r.roomStatus = 1")
    Optional<RoomOrder> roomOrderIspresentCheck(Long roomorderIdx);


    //현재 예약 요청들
    @Query("SELECT r FROM RoomOrder r where r.storeIdx = :storeIdx " +
            "and r.roomStatus = 3")
    Page<RoomOrder> roomOrderSearch(Pageable pageable,Long storeIdx);

    @Query("SELECT r FROM RoomOrder r where r.storeIdx = :storeIdx " +
            "and r.roomStatus = 3")
    List<RoomOrder> roomOrderSearch2(Long storeIdx);



    //room의 지난 내역(roomstatus=4)들 확인
    @Query("select r from RoomOrder r where r.roomIdx = :#{#searchDTO.roomIdx} " +
            "and r.roomStatus = 4")
    Page<RoomOrder> endRoomOrderSearch(Pageable pageable,SearchDTO searchDTO);


    //room의 현재 예약 정보 확인
    @Query("select r from RoomOrder r where r.roomIdx = :#{#searchDTO.roomIdx} " +
            "and r.roomStatus = 1")
    Optional<RoomOrder> roomOrderStatusCheck(SearchDTO searchDTO);



    //room의 현 예약자의 정보 확인
    @Query("SELECT m FROM Member m LEFT JOIN RoomOrder r ON r.memberIdx = m.memberIdx " +
            "left join Room a ON a.roomIdx = r.roomIdx where " +
            "r.roomStatus = 1")
    Optional<Member> roomOrderMemberCheck(SearchDTO searchDTO);

    Optional<RoomOrder> findByMemberIdx(Long memberIdx);

    List<RoomOrder> RoomOrderfindByMemberIdx(Long memberIdx);


}
