package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.Manager;
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


    private LocalDateTime regdate;

    private LocalDateTime moddate;

    private DistDTO distDTO;
    private ManagerDTO managerDTO;

    private String distCd;


    private List<StoreDTO> storeDTOList = new ArrayList<>();




}
