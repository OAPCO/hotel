package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.Store;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private AdminDTO adminDTO;

    private List<OrderDTO> orderDTOList;
    private List<StoreDTO> storeDTOList;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
