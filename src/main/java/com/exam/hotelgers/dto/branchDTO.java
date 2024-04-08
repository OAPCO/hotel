package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class branchDTO {

    private Long branchIdx;


    private String branchCd;
    private String branchName;

    private String branchChief;
    private String branchChiefEmail;
    private String branchTel;


    private distDTO distDTO;

    private BrandDTO brandDTO;

    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
