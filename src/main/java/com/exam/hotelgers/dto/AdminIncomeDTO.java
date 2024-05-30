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
public class AdminIncomeDTO {

    private Long adminIncomeIdx;

    private int incomePrice;

    private String incomeType;

    private Long storeIdx;
    private Long distIdx;
    private Long paymentIdx;

    private LocalDateTime regdate;

    private LocalDateTime moddate;

}
