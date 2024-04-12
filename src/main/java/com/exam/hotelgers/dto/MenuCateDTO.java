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
public class MenuCateDTO {
    private Long menuCateIdx;


    private String storeCd; //매장코드


    private String menuCateName; //메뉴 카테고리 이름


    private String tblStoreStoreIdx;//매장 식별키

    private LocalDateTime regdate;

    private LocalDateTime moddate;
}
