package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoomType;
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


    private DistDTO distDTO;
    private BranchDTO branchDTO;
    private StoreDTO storeDTO;


    private List<OrderDTO> orderDTOList;


    private RoomType roomType;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

}
