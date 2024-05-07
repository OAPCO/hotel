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
    private RoomDTO room;
    private StoreDTO store;
    private List<DetailmenuDTO> detailMenuList;
}
