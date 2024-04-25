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
@Table(name="menusheet")
@SequenceGenerator(
        name = "menusheet_sql",
        sequenceName = "menusheet_sql",
        initialValue = 1,
        allocationSize = 1
)
@QueryEntity
public class MenuSheet extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuoption_sql")
    private Long menuSheetIdx;//메뉴 옵션키
    private Integer newOrderNo;//신규 주문번호
    private Integer menuSheetState; //주문서 상태

}
