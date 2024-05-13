package com.exam.hotelgers.dto;

import lombok.*;

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
    private String checkinTime;
    private String checkoutTime;


    private String roomOrderType;
    private String peopleNum;


    private Long roomIdx;
    private Long memberIdx;
    private Long storeIdx;
    private int roomStatus;
    private String storeCd;


}
