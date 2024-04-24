package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.constant.StorePType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class StoreDTO {

    private Long storeIdx;

    private String storeCd; //매장코드
    private String storeChief; //대빵
    private String storeChiefEmail; //대빵이메일
    private String storeChieftel; //대빵전번
    private String major; //비콘 메이저코드
    private String minor; //비콘 마이너코드
    private String storeName; //매장명
    private String storePostNo; //우편번호
    private String storeAddr; //주소
    private String storeAddrDetail; //상세주소
    private BigDecimal storeLat; //위도
    private BigDecimal storeLng; //경도
    private String storeTel; //전화번호
    private String storeOpenTime; //영업시작시간
    private String storeCloseTime; //영업종료시간
    private String storeRestDay; //휴무일
    private String storeRestDetail;//휴무일 상세
    private String storeMessage; //공지사항

    private int storeOpenState; //0:영업중, 1:영업종료
    private int storePartnerState; //0:제휴중, 1:제휴종료
    private int storeBeaconState; //0: 반매장, 1:비콘
    private int storePaymentType; //0:선결제, 1:후결제
    private String cityCd; //도시 코드
    private String regionCd; //지역 코드
    private String bankNum; //계좌번호
    private String bankName; //은행명
    private String bankOwner; //예금주
    private int nowOrderNo; //매장별 주문 번호
    private LocalDateTime nowOrderNoDate; //매장별 주문 번호 기준일


    private String storeSummary; //매장 설명
    private String storeTag; //매장 태그
    private int storeServiceType; //1:매장, 2:테이크
    private String recommendYn; //추천
    private Character kakaoSendYn; //카카오톡 전송여부
    private Character deliveryFeeYn; //배달료여부
    private int deliveryFee; //배달료
    private int deliveryMinFee; //최소배달금액


    private String storeimgName;



    private StorePType storePType;
    private StoreStatus storeStatus;
    private StoreGrade storeGrade;


    private DistDTO distDTO;
    private BrandDTO brandDTO;

    private List<OrderDTO> orderDTOList;
    private List<RoomDTO> roomDTOList;
    private List<MenuCateDTO> menuCateDTOList;
    private List<DetailmenuDTO> detailmenuDTOList;


    private LocalDateTime regdate;
    private LocalDateTime moddate;



}
