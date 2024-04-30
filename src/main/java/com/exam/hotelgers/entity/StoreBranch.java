package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.entity.BaseEntity;
import com.exam.hotelgers.entity.StoreDist;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="store_branch")
@SequenceGenerator(
        name = "store_branch_sql",
        sequenceName = "store_branch_sql",
        initialValue = 1,
        allocationSize = 1
)
public class StoreBranch extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_branch_sql")
    private Long id;
    private String branchId;
    private Long storeBranchIdx;

    @Column(unique = true)
    private String storeBranchCd; //매장코드
    private String storeBranchName; //매장명
    private String storeBranchChief; //대빵
    private String storeBranchChiefEmail; //대빵이메일
    private String storeBranchChiefTel; //대빵전번
    private String storeBranchTel; //전화번호
    private String storeBranchMessage; //공지사항
    private String storeBranchRestDay; //휴무일
    private String storeBranchRestDetail;//휴무일 상세
    private String storeBranchSummary; //매장 설명
    private int storeBranchPaymentType; //0:선결제, 1:후결제
    private String storeBranchOpenTime; //영업시작시간
    private String storeBranchCloseTime; //영업종료시간
    private Character kakaoSendYn; //카카오톡 전송여부
    private String storeBranchAddr; //주소
    private String storeBranchAddrDetail; //상세주소
    private String major; //비콘 메이저코드
    private String minor; //비콘 마이너코드
    private String storeBranchPostNo; //우편번호
    private BigDecimal storeBranchLat; //위도
    private BigDecimal storeBranchLng; //경도
    private String storeBranchImgName;
    private int storeBranchOpenState; //0:영업중, 1:영업종료
    private int storeBranchPartnerState; //0:제휴중, 1:제휴종료
    private int storeBranchBeaconState; //0: 반매장, 1:비콘
    private String cityCd; //도시 코드
    private String regionCd; //지역 코드
    private String bankNum; //계좌번호
    private String bankName; //은행명
    private String bankOwner; //예금주
    private int nowOrderNo; //매장별 주문 번호
    private LocalDateTime nowOrderNoDate; //매장별 주문 번호 기준일
    private String storeBranchTag; //매장 태그
    private int storeBranchServiceType; //1:매장, 2:테이크
    private String recommendYn; //추천
    private Character deliveryFeeYn; //배달료여부
    private int deliveryFee; //배달료
    private int deliveryMinFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store; // Store 엔티티와의 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeDistIdx")
    private StoreDist storeDist; // StoreDist 엔티티와의 관계

    @Enumerated(EnumType.STRING)
    private StorePType storePType;

    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;

    @Enumerated(EnumType.STRING)
    private StoreGrade storeGrade;
}