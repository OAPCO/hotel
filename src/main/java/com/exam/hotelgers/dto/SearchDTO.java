package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.StoreStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SearchDTO {

    private List<DistDTO> distList;
    private List<BrandDTO> brandList;
    private List<MenuCateDTO> menuCateDTOList;

    //총판
    private Long distIdx;
    private String distCd;

    private String distName;
    private String distChief;
    private String distChiefEmail;
    private String distTel;




    //브랜드

    private Long brandIdx; //브랜드키
    private String brandCd; //브랜드코드

    private String brandName; //브랜드이름.

    //스토어
    private String storeChief;//매장주
    private Integer storePartnerState; //제휴상태

    private StoreStatus storestatus; //제휴상태 열거형 문자열
}
