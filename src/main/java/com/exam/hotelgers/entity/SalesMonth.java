package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="salesmonth")
@SequenceGenerator(
        name = "salesmonth_sql",
        sequenceName = "salesmonth_sql",
        initialValue = 1,
        allocationSize = 1
)
public class SalesMonth extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "salesmonth_sql")
    private Long salesmonthidx;//매출키
    private String distributor_organization;//총판조직
    private String Branch;//지사
    private String store;//매장
    private Integer Total_amount;//합산금액
    private Integer Offline_payment_amount;//오프라인 결제금액
    private Integer PG_payment_amount;//PG결제금액
    private Integer PG_fee;//PG수수료
    private Integer Fee_per_case;//건별수수료
    private Integer Monthly_fee;//월정수수료
    private Integer storeremittance;//매장송금액
    private LocalDate date;//결제일자
    //private LocalDate startDate;//시작날짜
    //private LocalDate endDate;//종료날짜
}
