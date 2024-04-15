package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="pointevent")
@SequenceGenerator(
        name = "pointevent_sql",
        sequenceName = "pointevent_sql",
        initialValue = 1,
        allocationSize = 1
)
public class PointEvent extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pointevent_sql")
    private Long pointEventIdx;//이벤트키

    private String pointEventName;//이벤트명

    private String pointEventType;//이벤트유형

    private String storeCd;//대상매장

    private String expiryDateYn;//유효기간여부

    private LocalDate eventStartDate;//이벤트 시작일

    private LocalDate eventEndDate;//이벤트 종료일

    private Integer eventPoint;//이벤트 포인트

    private String pointMemberType;//회원별 설정(ALL,MEMBER)

    private Integer minBuyMoney;//최소구매금액

    private Integer shopPayRate;//매장부담율
}
