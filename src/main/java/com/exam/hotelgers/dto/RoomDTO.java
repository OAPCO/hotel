package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class RoomDTO {


    private Long roomIdx;
    private String roomCd;

    private String roomName;
    private String roomMainimgName;
    private String roomimgName;
    private int roomStatus;
    private int roomPrice;

    private DistDTO distDTO;
    private StoreDTO storeDTO;
    private List<MenuOrderDTO> menuOrderDTOList;

    private String roomType;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

}
