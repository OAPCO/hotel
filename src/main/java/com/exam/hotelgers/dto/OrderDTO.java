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

    private Long orderIdx;//주문 키

    private String orderCd;//주문 코드(번호)


    private DistDTO distDTO;//총판DTO
    private StoreDTO storeDTO;//매장DTO
    private RoomDTO roomDTO;//룸DTO

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

}
