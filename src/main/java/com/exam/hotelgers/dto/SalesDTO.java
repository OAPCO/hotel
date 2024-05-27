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
public class SalesDTO {

    private Integer year;
    private Integer month;

    private Double totalSales;

    public SalesDTO(Integer year, Double totalSales) {
    }
}
