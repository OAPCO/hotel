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
    private Long menuOptionIdx;//메뉴옵션 키

    private String MenuOptionName;//메뉴옵션 명

    private int MenuPrice;//메뉴가격

    private int menuSalePercent;//메뉴 할인율

    private DetailmenuDTO detailmenuDTO;

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일
}
