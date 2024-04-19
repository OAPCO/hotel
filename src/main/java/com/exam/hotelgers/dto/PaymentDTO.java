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
    private Long paymentIdx; //결제키
    private String orderNo; //주문번호
    private String paymentNo;//결제번호
    private Integer type;//구분
    private String paymentPayType;//결제구분
    private String roomName;//룸명
    private Integer paymentCompleteState;//결제완료상태
    private String storename;//매장명
    private Integer ordercount;//주문수량
    private Integer count;//수량
    private String menu;//메뉴
    private String id;//아이디
    private Integer menuIdx;//메뉴번호
    private Integer price;//금액
    private LocalDateTime orderdate;//주문일시
    private LocalDateTime paymentdate;//결제일시
}
