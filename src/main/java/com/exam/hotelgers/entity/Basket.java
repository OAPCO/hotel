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
@Table(name="basket")
@SequenceGenerator(
        name = "bakset_sql",
        sequenceName = "basket_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Basket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "basket_sql")
    private Long basketIdx;

    @Column(length = 60, nullable = false)
    private String basketCd;


    private Long memberIdx;

}
