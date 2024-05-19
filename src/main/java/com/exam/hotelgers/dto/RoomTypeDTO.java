package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class RoomTypeDTO {


    private Long roomTypeIdx;

    private String roomTypeName;

    private String roomTypeMainimgName;

    private String roomTypeimgName;

    private String storeIdx;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

}
