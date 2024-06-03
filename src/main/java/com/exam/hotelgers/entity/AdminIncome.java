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
@Table(name="adminIncome")
@SequenceGenerator(
        name = "adminIncome_sql",
        sequenceName = "adminIncome_sql",
        initialValue = 1,
        allocationSize = 1
)
public class AdminIncome extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adminIncome_sql")
    private Long adminIncomeIdx;

    private int incomePrice;

    private String incomeType;

    private Long storeIdx;
    private Long distIdx;
    private Long paymentIdx;

}
