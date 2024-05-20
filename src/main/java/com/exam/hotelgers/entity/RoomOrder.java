package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="roomorder")
@SequenceGenerator(
        name = "roomorder_sql",
        sequenceName = "roomorder_sql",
        initialValue = 1,
        allocationSize = 1
)
public class RoomOrder extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomorder_sql")
    private Long roomorderIdx;

    //추후 없앨 칼럼 두개.
    private String startTime;
    private String endTime;

    //예약 희망 날짜
    private int reservationDateCheckin;
    private int reservationDateCheckout;
    
    //실제 체크인/아웃 날짜,시간
    private String checkinTime;
    private String checkoutTime;

    //예약한 방의 타입
    private String roomOrderType;
    
    //머릿수
    private String peopleNum;

    //가격
    private int roomPrice;


    private Long roomIdx;
    private Long memberIdx;
    private String memberName;
    private Long storeIdx;
    private int roomStatus;
    private String storeCd;

}
