package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailmenuDTO {
    private Long detailmenuIdx; //메뉴키

    private String detailMenuName; //메뉴명
    @Column(length = 2)
    private String optionYn; //옵션여부 n
    @Column(length = 200)
    private String menuImg; //메뉴 이미지
    @Column(length = 500)
    private String menuDescription; //메뉴설명

    private Long menuCateIdx; //메뉴상세 식별키

    private String menuprice; //메뉴 가격

    private char salesYn; //판매여부 Y:판매중 N:품절

    private char deliveryYn; //배달여부 Y:배달가능 N:배달불가
    @Column(length = 30)
    private String menuSaleType; //할인구분 NoNE:없음, AMOUNT:
    @Column(length = 11)
    private int menuSaleAmount; //할인액
    @Column(length = 11)
    private int menuSalePercent; //할인율
    @Column(length = 30)
    private String pointSaveType; //포인트 적립구분
    @Column(length = 11)
    private int pointSaveAmount; //포인트 적립액
    @Column(length = 11)
    private int pointSavePercent; //포인트 적립율

    private LocalDateTime regdate;

    private LocalDateTime moddate;
}
