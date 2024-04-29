package com.exam.hotelgers.entity;

import com.exam.hotelgers.entity.BaseEntity;
import com.exam.hotelgers.entity.StoreBranch;
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
@Table(name = "store_dist")
@SequenceGenerator(
        name = "storedist_sql",
        sequenceName = "storedist_sql",
        initialValue = 1,
        allocationSize = 1
)
public class StoreDist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storedist_sql")
    private Long storeDistIdx;

    @Column(length = 200, unique = true)
    private String storeDistCode;

    @Column(length = 45)
    private String storeDistName;

    @Column(length = 50)
    private String storeDistChief;

    @Column(length = 200)
    private String storeDistTel;

    @OneToMany(mappedBy = "storeDist", cascade = CascadeType.ALL)
    private List<StoreBranch> storeBranchList = new ArrayList<>();


    // StoreDist 엔티티와의 양방향 매핑
    @OneToMany(mappedBy="parentStoreDist", cascade = CascadeType.ALL)
    private List<StoreDist> childStoreDistList = new ArrayList<>();

    // StoreDist 엔티티와의 양방향 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_store_dist_idx")
    private StoreDist parentStoreDist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;

}