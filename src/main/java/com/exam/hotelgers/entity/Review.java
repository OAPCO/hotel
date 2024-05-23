package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.Rate;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="review")
@SequenceGenerator(
        name = "review_sql",
        sequenceName = "review_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Review  extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_sql")
    private Long reviewIdx;

    private Rate rate;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeIdx")
    private Store store;


}
