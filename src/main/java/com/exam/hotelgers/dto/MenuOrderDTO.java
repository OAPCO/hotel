package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOrderDTO {

    private Long menuorderIdx;

    private Long menuorderCd;

    private Long total;

    private Long memberIdx;
    private Long orderState;//주문서 상태 0.결제완료 1.조리완료 2.배달완료 3.결제취소
    private String orderRequest;

    private Long storeIdx;

    private Long roomIdx;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

    private List<MenuSheetDTO> menuSheets;
}
