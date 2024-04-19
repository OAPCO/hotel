package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="dist")
@SequenceGenerator(
        name = "dist_sql",
        sequenceName = "dist_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Dist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dist_sql")
    private Long distIdx;

    @Column(length = 200, unique = true)
    private String distCd;

    @Column(length = 45)
    private String distName;
    @Column(length = 50)
    private String distChief;
    @Column(length = 200)
    private String distTel;
    @Column(length = 100)
    private String distChiefEmail;


    @OneToMany(mappedBy="dist", cascade = CascadeType.ALL)
    private List<Branch> branchList = new ArrayList<>();

    @OneToMany(mappedBy="dist", cascade = CascadeType.ALL)
    private List<Store> storeList = new ArrayList<>();

    @OneToMany(mappedBy="dist", cascade = CascadeType.ALL)
    private List<Brand> brandList = new ArrayList<>();

    @OneToMany(mappedBy="dist", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="brandIdx")
    private Brand brand;










}
