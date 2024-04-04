package com.exam.hotelgers.entity;

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
@Table(name="store")
@SequenceGenerator(
        name = "store_sql",
        sequenceName = "store_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Store extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_sql")
    private Long storeIdx;

    private int storeCateIdx;
    private int brandIdx;
    private String storeCd;
    private String major;
    private String minor;
    private int storeMemberIdx;
    private String storeName;
    private String storePostNo;
    private String storeAddr;
    private String storeAddrDetail;
    private BigDecimal storeLat;
    private BigDecimal storeLng;
    private String storeTel;
    private String storeOpenTime;
    private String storeCloseTime;
    private String storeRestDay;
    private String storeMessage;
    private String storeImg;
    private int storeImgWidth;
    private int storeImgHeight;
    private int storeOpenState;
    private int storePartnerState;
    private int storeBeaconState;
    private int storeType;
    private int storePaymentType;
    private String cityCd;
    private String regionCd;
    private String bankNum;
    private String bankName;
    private String bankOwner;
    private int nowOrderNo;
    private LocalDateTime nowOrderNoDate;
    private Character delYn;
    private int tblBrandBrandIdx;
    private int tblStoreCateStoreCateIdx;
    private String storeSummary;
    private String storeTag;
    private String bannerYn;
    private String mainYn;
    private int storeServiceType;
    private String recommendYn;
    private Character kakaoSendYn;
    private Character deliveryFeeYn;
    private int deliveryFee;
    private int storeBranchIdx;
    private int deliveryMinFee;
    private Character pointSaveYn;
    private String mangoStoreCode;
    private String mangoCatId;






}
