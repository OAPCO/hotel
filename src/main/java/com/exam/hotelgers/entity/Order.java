package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="order")
@SequenceGenerator(
        name = "order_sql",
        sequenceName = "order_sql",
        initialValue = 1,
        allocationSize = 1
)
@QueryEntity
public class Order extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sql")
    private Long orderIdx;

    private String orderCd; //임시


    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="branchIdx")
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="distIdx")
    private Dist dist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brandIdx")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;




}
