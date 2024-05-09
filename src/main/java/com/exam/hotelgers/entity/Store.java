package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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



    @Column(unique = true)
    private String storeCd; //매장코드
    private String storeName; //매장명
    private String storeTel; //전화번호
    private String storeMessage; //공지사항
    private String storeRestDay; //휴무일
    private String storeRestDetail;//휴무일 상세
    private String storeSummary; //매장 설명
    private int storePaymentType; //0:선결제, 1:후결제
    private String storeOpenTime; //영업시작시간
    private String storeCloseTime; //영업종료시간


    private Character kakaoSendYn; //카카오톡 전송여부
    private String storeAddr; //주소
    private String storeAddrDetail; //상세주소




    private String major; //비콘 메이저코드
    private String minor; //비콘 마이너코드

    private String storePostNo; //우편번호
    private BigDecimal storeLat; //위도
    private BigDecimal storeLng; //경도


    private String storeimgName;





    private int storeOpenState; //0:영업중, 1:영업종료
    private int storePartnerState; //0:제휴중, 1:제휴종료
    private int storeBeaconState; //0: 반매장, 1:비콘
    private String cityCd; //도시 코드
    private String regionCd; //지역 코드
    private String bankNum; //계좌번호
    private String bankName; //은행명
    private String bankOwner; //예금주
    private int nowOrderNo; //매장별 주문 번호
    private LocalDateTime nowOrderNoDate; //매장별 주문 번호 기준일


    private String storeTag; //매장 태그
    private int storeServiceType; //1:매장, 2:테이크
    private String recommendYn; //추천
    private Character deliveryFeeYn; //배달료여부
    private int deliveryFee; //배달료
    private int deliveryMinFee; //최소배달금액
    private String facilities; //편의시설들


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="distIdx")
    private Dist dist;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="brandIdx")
//    private Brand brand;

    private String brandCd;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="managerIdx")
//    private Manager manager;
    private String managerId;


    @ToString.Exclude
    @OneToMany(mappedBy="store", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();
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
    private List<RoomOrder> roomOrderList = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private StorePType storePType;

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Enumerated(EnumType.STRING)
    private StoreGrade storeGrade;




}
