package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="menucate")
@SequenceGenerator(
        name = "menucate_sql",
        sequenceName = "menucate_sql",
        initialValue = 1,
        allocationSize = 1
)
public class MenuCate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menucate_sql")
    private Long menuCateIdx;//메뉴 카테고리 키

    @Column(length = 200)
    private String menuCateName; //메뉴 카테고리 이름

    private String menuCateimgName;//메뉴 카테고리 이미지 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;//매장

    @Column(length = 200)
    private String tblStoreStoreIdx; //참조용 이름

    @OneToMany(mappedBy="menuCate", cascade = CascadeType.ALL)
    private List<Detailmenu> detailMenuList = new ArrayList<>();//메뉴 상세 리스트


}
