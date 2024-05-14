package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.Room;
import com.exam.hotelgers.entity.Store;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuSheetDTO {
    private Long menuSheetIdx;//메뉴 옵션키

    private Long orderIdx;//묶인 주문번호

    private String menuorderName;//주문메뉴
    private Long menuorderPrice; //주문메뉴단일가격
    private Long menuorderQuantity; //주문갯수
    private Long AmountPrice;//메뉴단일가격*갯수=해당매뉴총가격



}
