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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeorder_sql")
    private Long orderIdx;//주문 키

    @Column(length = 60, nullable = false)
    private String orderCd;//주묹 코드(번호)


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="distIdx")
    private Dist dist;//총판

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;//매장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomIdx")
    private Room room;//룸(방)


}
