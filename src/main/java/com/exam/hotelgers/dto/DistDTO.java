package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class DistDTO {

    private Long distIdx;

    private String distCd;


    private String distName;
    private String distChief;
    private String distChiefEmail;
    private String distTel;

    private List<BranchChiefDTO> branchChiefDTOList;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
