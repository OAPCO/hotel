package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="payment")
@SequenceGenerator(
        name = "payment_sql",
        sequenceName = "payment_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_sql")
    private Long paymentIdx; //결제키
    @Column(length = 200)
    private String offOrderSheetNo; //오프라인 주문번호
    @Column(length = 200)
    private String paymentNo;//결제내역번호
    @Column(length = 11)
    private int paymentPrice;//총결제금액
    @Column(length = 11)
    private int dspositPrice;//업체입금액
    @Column(length = 11)
    private int pgFeePrice;//pg수수료
    @Column(length = 11)
    private int dayFeePrice;//건별 수수료
}
