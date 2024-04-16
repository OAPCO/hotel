package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesDTO {
    private Long salesidx;//매출키
    private String distributor_organization;//총판조직
    private String Branch;//지사
    private String storecd;//매장코드
    private String store;//매장
    private String storename;//매장명
    private String ordernum;//주문번호
    private String paymentnum;//결제번호
    private String id;//회원ID
    private Integer payment;//결제금액
    private String payment_method;//결제방식
    private Integer PG;//PG수수료
    private Integer storeremittance;//매장송금액
    private Integer net_sales;//순매출
    private String processing_status;//처리상태
    private LocalDate date;//결제일자
    private LocalDate startDate;//시작날짜
    private LocalDate endDate;//종료날짜
}
