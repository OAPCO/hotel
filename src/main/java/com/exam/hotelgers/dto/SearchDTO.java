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

    //검색용 별도 컬럼
    private String name;
    private String id;

    //열거형,날짜,사진 등
    private RoleType roleType;
    private StorePType storePType;
    private StoreStatus storeStatus;
    private StoreGrade storeGrade;
    private RoomType roomType;
    private String storeimgName;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


    //어드민
    private Long adminIdx;
    private String adminId;
    private Integer adminLevel;
    private String adminName;
    private String adminPhone;
    private String adminEmail;


    //총판장
    private Long distChiefIdx;
    private String distChiefId;
    private Integer distChiefLevel;
    private String distChiefName;
    private String distChiefPhone;
    private String distChiefEmail;


    //매니저
    private Long managerIdx;
    private String managerId;
    private String password;
    private Integer managerLevel;
    private String managerName;
    private String managerPhone;
    private String managerEmail;


    //회원
    private Long memberIdx;
    private Character memberJoinType; //"회원가입타입(C:일반,K:카카오톡,F:페이스북,N:네이버
    private String memberType; //회원타입
    private String memberEmail;//"회원 이메일
    private String memberNickname;//"회원 별명
    private String memberId;
    private String memberName;
    private String memberNum;
    private String memberPhone;//회원 전화번호


    //문의
    private Long qnaIdx;
    private String qnaTitle;
    private String qnaContent;
    private int qnaStatus;




    //총판
    private Long distIdx;
    private String distCd;
    private String distName;
    private String distChief;
    private String distTel;


    //브랜드
    private Long brandIdx;
    private String brandCd;
    private String brandName;


    //스토어
    private Long storeIdx;
    private String storeCd; //매장코드
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
    private Long roomIdx;
    private String roomCd;
    private String roomName;



    //주문
    private Long orderIdx;
    private String orderCd;





}
