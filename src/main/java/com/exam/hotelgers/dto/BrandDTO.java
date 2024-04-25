package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDTO {

    private Long brandIdx; //브랜드키

    private String brandCd; //브랜드코드

    private String brandName; //브랜드이름.


    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

    private DistDTO distDTO;//총판DTO

    private List<StoreDTO> storeDTOList = new ArrayList<>();//매장DTO 목록







//    private Long distIdx; //매장총판키(호텔키)
//
//    private String  distId; //총판ID(호텔ID)

//    private Long tblStoreMemberStoreMemberIdx; //매장회원 키


//    private Long storeCateIdx; //매장 카테고리키
//
//    private Long storeMemberIdx; //매장회원키

}
