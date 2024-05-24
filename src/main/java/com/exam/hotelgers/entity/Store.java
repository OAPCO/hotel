package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="store")
@SequenceGenerator(
        name = "store_sql",
        sequenceName = "store_sql",
        initialValue = 1,
        allocationSize = 1
)
@QueryEntity
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_sql")
    private Long storeIdx;

    private String storeCheckinTime;
    private String storeCheckoutTime;

    @Column(unique = true)
    private String storeCd; //매장코드
    private String storeName; //매장명
    private String storeTel; //전화번호
    private String storeMessage; //공지사항
    private String storeSummary; //매장 설명
    private int storePaymentType; //0:선결제, 1:후결제


    private String storeAddr; //주소
    private String storeAddrDetail; //상세주소


    private String storePostNo; //우편번호
    private BigDecimal storeLat; //위도
    private BigDecimal storeLng; //경도

    private String storeimgName;

    private int nowOrderNo; //매장별 주문 번호
    private LocalDateTime nowOrderNoDate; //매장별 주문 번호 기준일
    private String facilities; //편의시설들
    private String regionCd; //지역 코드


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="distIdx")
    private Dist dist;


    private String brandCd;
    private String managerId;


    @ToString.Exclude
    @OneToMany(mappedBy="store", cascade = CascadeType.ALL)
    private List<Room> roomList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy="store", cascade = CascadeType.ALL)
    private List<MenuCate> menuCateList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy="store", cascade = CascadeType.ALL)
    private List<Detailmenu> detailMenuList = new ArrayList<>();


    @ToString.Exclude
    @OneToMany(mappedBy="store", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StorePType storePType;

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Enumerated(EnumType.STRING)
    private StoreGrade storeGrade;




}
