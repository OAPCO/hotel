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
    private String distTel;

    private List<StoreDTO> storeDTOList;
//    private List<BrandDTO> brandDTOList;
    private List<MenuOrderDTO> menuOrderDTOList;
    private List<ManagerDTO> managerDTOList;

    private DistChiefDTO distChiefDTO;

    private int storeCount;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
