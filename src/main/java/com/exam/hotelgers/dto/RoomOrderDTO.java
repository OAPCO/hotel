package com.exam.hotelgers.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomOrderDTO {
    private Long roomorderIdx;
    private String userName;
    private String userPhone;
    private String startTime;
    private String endTime;
    private String roomCd;
}
