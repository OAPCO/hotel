package com.exam.hotelgers.entity;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private Integer menuSheetState;//주문서 상태 0.주문전, 1.조리요청, 2.결제요청, 3.결제완료, 4.결제취소, 5.조리완료, 6.배달요청, 7.배달완료
    private String menuSheetName;//주문서 이름
    private LocalDateTime orderdate;//조리 요청일
    private String orderProgressStatus;//주문상태(NEW 신규,CHECK 접수,CANCEL 취소,CALL 호출,CLOSE 완료)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;//매장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomIdx")
    private Room room;//룸(방)

}
