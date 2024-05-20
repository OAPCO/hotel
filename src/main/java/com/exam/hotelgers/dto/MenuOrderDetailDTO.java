package com.exam.hotelgers.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDetailDTO extends MenuOrderDTO{
    private String storeName;
    private String storeCd;
    private String roomName;
    private String roomCd;
    private String memberName;

}
