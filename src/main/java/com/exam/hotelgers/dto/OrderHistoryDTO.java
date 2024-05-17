package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.entity.MenuOrder;
import com.exam.hotelgers.entity.RoomOrder;
import com.exam.hotelgers.entity.Store;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistoryDTO {
    private RoomOrder roomOrder;
    private MenuOrder menuOrder;
    private Member member;
    private Store store;
    private String storeName;
}
