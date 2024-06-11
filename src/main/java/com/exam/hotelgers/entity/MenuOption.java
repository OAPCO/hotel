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
    private Long menuOptionIdx;

    private String MenuOptionName;

    private int MenuPrice;

    private int menuSalePercent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="detailmenuIdx")
    private Detailmenu detailmenu;
}
