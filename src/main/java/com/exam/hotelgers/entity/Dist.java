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
    private Long distIdx;//총판 키

    @Column(length = 200, unique = true)
    private String distCd;//총판코드

    @Column(length = 45)
    private String distName;//총판명
    @Column(length = 200)
    private String distTel;//총판연락처


    private String imgName;//이미지명




    @OneToMany(mappedBy="dist", cascade = CascadeType.ALL)
    private List<Store> storeList = new ArrayList<>();//매장 목록

    @OneToMany(mappedBy="dist", cascade = CascadeType.ALL)
    private List<Brand> brandList = new ArrayList<>();//브랜드 목록

    @OneToMany(mappedBy="dist", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();//주문 목록


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="distChiefIdx")
    private DistChief distChief;//총판장



}
