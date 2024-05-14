package com.exam.hotelgers.entity;


import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="storeorder")
@SequenceGenerator(
        name = "storeorder_sql",
        sequenceName = "storeorder_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Order extends BaseEntity {
    //Todo 1.메뉴 2.갯수 3.가격 4.갯수*가격=총결제금액 5.결제수단 6.주문시간v 7.주문번호v 8.주문자 정보v 9.방 idxv 10.매장 idxv
    //주문 따로, 주문 상세보기 따로. orderIdx에 묶어서 menusheet에 상세주문. 메뉴이름 가격 갯수 합, 총 결제 금액은 order에.
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeorder_sql")
    private Long orderIdx;

    @Column(length = 60, nullable = false)
    private Long orderCd; //주문번호

    private Long total; //총 결제 금액

    private Long memberIdx; //결제자 정보
    private Long orderState;//주문서 상태 0.결제완료 1.조리완료 2.배달완료 3.결제취소
    private String orderRequest;//주문요청사항



    private Long storeIdx;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomIdx")
    private Room room;


}
