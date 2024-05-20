package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.*;
import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistoryDTO {
    List<RoomOrderDetailDTO> roomOrderDetailList;
    List<MenuOrder> menuOrderList;

}
