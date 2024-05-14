package com.exam.hotelgers.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="menusheet")
@SequenceGenerator(
        name = "menusheet_sql",
        sequenceName = "menusheet_sql",
        initialValue = 1,
        allocationSize = 1
)
public class MenuSheet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menusheet_sql")
    private Long menuSheetIdx;//메뉴 시트 키



    private String menuorderName;//주문메뉴

    private Long menuorderPrice; //주문메뉴단일가격

    private Long menuorderQuantity; //주문갯수

    private Long AmountPrice;//메뉴단일가격*갯수=해당매뉴총가격

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuorderIdx")
    private MenuOrder menuOrder;//묶인 주문번호
}
