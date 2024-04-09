package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import lombok.*;

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

//    private int tblMenuSheetMenuSheetldx1;
}
