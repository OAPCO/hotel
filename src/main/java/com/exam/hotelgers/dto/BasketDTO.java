package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketDTO {

    private Long basketIdx;

    private String basketCd;

    private Long memberIdx;

    private LocalDateTime regdate;
    private LocalDateTime moddate;

}
