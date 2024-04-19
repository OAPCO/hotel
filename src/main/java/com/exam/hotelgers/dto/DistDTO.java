package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private AdminDTO adminDTO;
    private BrandDTO brandDTO;


    private List<OrderDTO> orderDTOList;
    private List<BranchDTO> branchDTOList;
    private List<StoreDTO> storeDTOList;
    private List<BrandDTO> brandDTOList;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
