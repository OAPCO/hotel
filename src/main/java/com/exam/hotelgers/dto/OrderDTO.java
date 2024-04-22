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

    private String orderCd;


    private DistDTO distDTO;
    private StoreDTO storeDTO;
    private RoomDTO roomDTO;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

}
