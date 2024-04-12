package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class BranchDTO {

    private Long branchIdx;


    private String branchCd;
    private String branchName;

    private String branchChief;
    private String branchChiefEmail;
    private String branchTel;


    private DistDTO distDTO;

    private BrandDTO brandDTO;

    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
