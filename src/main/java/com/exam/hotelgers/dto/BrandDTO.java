package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDTO {

    private Long brandIdx; //브랜드키


    private String brandName; //브랜드이름.


    private LocalDateTime regdate;

    private LocalDateTime moddate;

    private StoreDistDTO storeDistDTO;

    private String brandCd; //매장코드





//    private Long storeDistIdx; //매장총판키(호텔키)
//
//    private String  storeDistId; //총판ID(호텔ID)

//    private Long tblStoreMemberStoreMemberIdx; //매장회원 키


//    private Long storeCateIdx; //매장 카테고리키
//
//    private Long storeMemberIdx; //매장회원키

}
