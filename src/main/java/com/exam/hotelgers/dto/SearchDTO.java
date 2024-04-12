package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SearchDTO {

    private List<BranchDTO> branchList;
    private List<DistDTO> distList;
    private List<BrandDTO> brandList;


    //총판
    private Long distIdx;
    private String distCd;

    private String distName;
    private String distChief;
    private String distChiefEmail;
    private String distTel;


    //지사
    private Long branchIdx;
    private String branchCd;

    private String branchName;
    private String branchChief;
    private String branchChiefEmail;
    private String branchTel;




    //브랜드

    private Long brandIdx; //브랜드키
    private String brandCd; //브랜드코드

    private String brandName; //브랜드이름.


}
