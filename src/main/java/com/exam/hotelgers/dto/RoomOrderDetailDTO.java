package com.exam.hotelgers.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RoomOrderDetailDTO extends RoomOrderDTO {
    private String storeName;
    private String roomName;
}
