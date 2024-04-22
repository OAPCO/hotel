package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuOptionDTO {
    private Long menuOptionIdx;

    private String MenuOptionName;

    private int MenuPrice;

    private int menuSalePercent;

    private DetailmenuDTO detailmenuDTO;

    private LocalDateTime regdate;
    private LocalDateTime moddate;
}
