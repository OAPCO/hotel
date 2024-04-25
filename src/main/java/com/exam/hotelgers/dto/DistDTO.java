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

    private Long distIdx;//총판키

    private String distCd;//총판 코드(번호)
    private String distName;//총판명
    private String distTel;//총판 연락처

    private List<StoreDTO> storeDTOList;//매장DTO 목록
    private List<BrandDTO> brandDTOList;//브랜드DTO 목록
    private List<OrderDTO> orderDTOList;//주문DTO 목록

    private DistChiefDTO distChiefDTO;//총판장DTO


    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일


}
