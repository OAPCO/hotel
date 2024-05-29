package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.constant.StorePType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class StoreDTO {

    private Long storeIdx;

    private String storeCheckinTime;
    private String storeCheckoutTime;

    private String storeCd; //매장코드
    private String storeName; //매장명
    private String storeAddr; //주소
    private String storeAddrDetail; //상세주소
    private BigDecimal storeLat; //위도
    private BigDecimal storeLng; //경도
    private String storeTel; //전화번호
    private String storeMessage; //공지사항

    private String regionCd; //지역 코드
    private int nowOrderNo; //매장별 주문 번호
    private double cancelCharge;

    private LocalDateTime nowOrderNoDate; //매장별 주문 번호 기준일

    private int roomCount;



    private String storeSummary; //매장 설명
    private String facilities; //편의시설들

    private String storeimgName; //스토어 이미지
    private double averageRating;//평균별점


    private StorePType storePType;
    private StoreStatus storeStatus;
    private StoreGrade storeGrade;






    private DistDTO distDTO;
    private BrandDTO brandDTO;
    private ManagerDTO managerDTO;

    private String ManagerId;
    private String brandCd;


    private List<MenuOrderDTO> orderDTOList;
    private List<RoomDTO> roomDTOList;

    private List<MenuCateDTO> menuCateDTOList;
    private List<DetailmenuDTO> detailmenuDTOList;


    private LocalDateTime regdate;
    private LocalDateTime moddate;



}
