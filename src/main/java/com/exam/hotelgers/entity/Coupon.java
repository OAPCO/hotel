package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="coupon")
@SequenceGenerator(
        name = "coupon_sql",
        sequenceName = "coupon_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Coupon extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_sql")
    private Long couponIdx; //키

    private String couponName; //쿠폰명

    private String couponPrice;//할인액

    private String couponState;//사용여부

    private String detail; //상세설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberEmail", referencedColumnName="memberEmail")
    private Member member; //멤버에 묶임
}
