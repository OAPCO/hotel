package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    private Long orderIdx;

    private Long orderCd;

    private Long total;

    private Long memberIdx;
    private Long orderState;//주문서 상태 0.결제완료 1.조리완료 2.배달완료 3.결제취소
    private String orderRequest;

    private Long storeIdx;

    private Long roomIdx;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

}
