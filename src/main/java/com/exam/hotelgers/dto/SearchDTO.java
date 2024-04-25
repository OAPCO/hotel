package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.*;
import jakarta.persistence.Column;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SearchDTO {

    //열거형,날짜,사진 등
    private RoleType roleType;//회원 등급
    private StorePType storePType;//매장 종류
    private StoreStatus storeStatus;//매장상태
    private StoreGrade storeGrade;//매장 등급
    private RoomType roomType;//룸 종료
    private String storeimgName;//매장 이미지 명


    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일


    //어드민
    private Long adminIdx;//관리자 키
    private String adminId;//관리자 아이디
    private Integer adminLevel;//관리자 단계
    private String adminName;//관리자 명
    private String adminPhone;//관리자 연락처
    private String adminEmail;//관리자 이메일


    //총판장
    private Long distChiefIdx;//총판장 키
    private String distChiefId;//총판장 아이디
    private Integer distChiefLevel;//총판장 단계
    private String distChiefName;//총판장 명
    private String distChiefPhone;//총판장 이미지
    private String distChiefEmail;//총판장 이메일



    //총판
    private Long distIdx;//총판키
    private String distCd;//총판코드(번호)
    private String distName;//총판명
    private String distChief;//총판장
    private String distTel;//총판 연락처


    //브랜드
    private Long brandIdx;//브랜드 키
    private String brandCd;//브랜드 코드(번호)
    private String brandName;//브랜드 명


    //스토어
    private Long storeIdx;//매장 키
    private String storeCd; //매장코드
    private String storeChief; //대빵
    private String storeChiefEmail; //대빵이메일
    private String storeChiefPhone; //대빵전번
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


    //룸
    private Long roomIdx;//룸 키
    private String roomCd;//룸코드 (번호)
    private String roomName;//룸명



    //주문
    private Long orderIdx;//주문키
    private String orderCd;//주문 코드(번호)





}
