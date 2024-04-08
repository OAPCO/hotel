package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="brand")
@SequenceGenerator(
        name = "brand_sql",
        sequenceName = "brand_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Brand extends BaseEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_sql")
    private Long brandIdx; //브랜드키



    @Column(length=200, nullable = false)
    private String brandName; //브랜드 이름

    @Column(unique = true)
    private String brandCd; //매장코드


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeDistIdx")
    private StoreDist storeDist;


    @OneToMany(mappedBy="brand", cascade = CascadeType.ALL)
    private List<Store> storeList = new ArrayList<>();

    @OneToMany(mappedBy="brand", cascade = CascadeType.ALL)
    private List<StoreBranch> storeBranchList = new ArrayList<>();


}
