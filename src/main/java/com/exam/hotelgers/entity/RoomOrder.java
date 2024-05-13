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
