package com.exam.hotelgers.entity;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="menuoption")
@SequenceGenerator(
        name = "menuoption_sql",
        sequenceName = "menuoption_sql",
        initialValue = 1,
        allocationSize = 1
)
@QueryEntity
public class MenuOption extends BaseEntity{
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuoption_sql")
    private Long menuOptionIdx;//메뉴 옵션키

    private String MenuOptionName;//메뉴옵션 명

    private int MenuPrice;//메뉴옵션가격

    private int menuSalePercent;//메뉴할인율

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="detailmenuIdx")
    private Detailmenu detailmenu;//상세메뉴
}
