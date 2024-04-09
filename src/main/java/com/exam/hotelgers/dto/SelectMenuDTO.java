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
public class SelectMenuDTO {
    private long selectMenuIdx;

    private String menuSheetNo;

    private int menuOptionIdx;

    private int menuPrice;

    private int memberIdx;

    private LocalDateTime regdate;

    private LocalDateTime moddate;

//    private int tblMenuSheetMenuSheetldx1;
}
