package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="branch")
@SequenceGenerator(
        name = "branch_sql",
        sequenceName = "branch_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Branch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_sql")
    private Long branchIdx;

    @Column(length = 45, unique = true)
    private String branchCd;



    @Column(length = 45)
    private String branchName;
    @Column(length = 50)
    private String branchChief;
    @Column(length = 200)
    private String branchChiefEmail;
    @Column(length = 30)
    private String branchTel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="distIdx")
    private Dist dist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brandIdx")
    private Brand brand;

    @OneToMany(mappedBy="branch", cascade = CascadeType.ALL)
    private List<Store> storeList = new ArrayList<>();

    @OneToMany(mappedBy="branch", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();



}
