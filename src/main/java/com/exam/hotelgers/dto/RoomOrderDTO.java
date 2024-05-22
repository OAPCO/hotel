package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomOrderDTO {

    private Long roomorderIdx;

    private String startTime;
    private String endTime;

    private String reservationDateCheckin;
    private String reservationDateCheckout;
    private LocalDateTime reservationDateCheckinDate;
    private LocalDateTime reservationDateCheckoutDate;
    private String checkinTime;
    private String checkoutTime;


    private String roomOrderType;
    private String peopleNum;


    private Long roomIdx;
    private Long memberIdx;
    private String memberName;
    private Long storeIdx;
    private String storeCd;


    private int roomStatus;
    private int roomPrice;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
