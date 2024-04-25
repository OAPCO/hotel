package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoomType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class RoomDTO {


    private Long roomIdx;//룸키
    private String roomCd;//룸 코드(번호)

    private String roomName;//룸명


    private DistDTO distDTO;//총판DTO
    private StoreDTO storeDTO;//매장DTO

    private String roomimgName;//룸 이미지 명

    private List<OrderDTO> orderDTOList;//주문DTO 목록


    private RoomType roomType;//룸 등급

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

}
