package com.exam.hotelgers.dto;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

    private Long paymentIdx;

    private int paymentPrice;
    private int paymentTotalPrice;
    private int paymentStatus;

    private String memberName;
    private String memberPhone;
    private String memberEmail;
    private String memberZipCode;
    private String memberAddress;

    //주문시 상품명. "객실예약" or "룸서비스" 으로 들어감.
    private String paymentName;
    //room = 객실예약 결제, service = 룸서비스 결제
    private String paymentType;

    private Long storeIdx;
    private Long distIdx;
    private Long roomorderIdx;
    private Long menuorderIdx;

    private LocalDateTime regdate;

    private LocalDateTime moddate;
}
