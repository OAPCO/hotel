package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="storebranch")
@SequenceGenerator(
        name = "storebranch_sql",
        sequenceName = "storebranch_sql",
        initialValue = 1,
        allocationSize = 1
)
public class StoreBranch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storebranch_sql")
    private Long storeBranchIdx;

    @Column(length = 45, unique = true)
    private String storeBranchId;



    @Column(length = 45)
    private String storeBranchName;
    @Column(length = 50)
    private String storeBranchChief;
    @Column(length = 200)
    private String storeBranchChiefEmail;
    @Column(length = 30)
    private String storeBranchTel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeDistIdx")
    private StoreDist storeDist;


}
