package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.entity.Order;
import com.exam.hotelgers.entity.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
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
    private String distTel;

    private List<StoreDTO> storeDTOList;
    private List<BrandDTO> brandDTOList;
    private List<OrderDTO> orderDTOList;

    private DistChiefDTO distChiefDTO;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
