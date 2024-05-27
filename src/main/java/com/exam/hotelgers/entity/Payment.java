package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="payment")
@SequenceGenerator(
        name = "payment_sql",
        sequenceName = "payment_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sql")
    private Long paymentIdx;

    private String paymentPrice;

    private String memberName;
    private String memberPhone;
    private String memberEmail;
    private String memberZipCode;
    private String memberAddress;

    private Long storeIdx;
    private Long distIdx;

    //주문시 상품명. 호텔명이 들어감.
    private String paymentName;
    //room = 객실예약 결제, service = 룸서비스 결제
    private String paymentType;


}
