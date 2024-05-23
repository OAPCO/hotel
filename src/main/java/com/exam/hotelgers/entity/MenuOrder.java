package com.exam.hotelgers.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="menuorder")
@SequenceGenerator(
        name = "menuorder_sql",
        sequenceName = "menuorder_sql",
        initialValue = 1,
        allocationSize = 1
)
public class MenuOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuorder_sql")
    private Long menuorderIdx;

    @Column(length = 60, nullable = false)
    private String menuorderCd; //주문번호

    private String total; //총 결제 금액

    private Long memberIdx; //결제자 정보
    private String orderState;//주문서 상태 0.결제완료 1.조리완료 2.배달완료 3.결제취소
    private String orderRequest;//주문요청사항
    private Long storeIdx;
    private Long roomorderIdx;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomIdx")
    private Room room;

    @OneToMany(mappedBy = "menuOrder", cascade = CascadeType.ALL)
    private List<MenuSheet> menuSheetList = new ArrayList<>();

}
