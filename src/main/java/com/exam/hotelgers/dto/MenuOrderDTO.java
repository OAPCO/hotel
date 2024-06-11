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

    private String menuorderCd;

    private String total;

    private Long memberIdx;
    private String orderState;//주문서 상태 0.결제완료 1.조리완료 2.배달완료 3.결제취소
    private String orderRequest;
    private Long roomorderIdx;

    private Long storeIdx;

    private Long roomIdx;
    private String roomName;
    private String memberName;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

    private List<MenuSheetDTO> menuSheetDTOList;
}
