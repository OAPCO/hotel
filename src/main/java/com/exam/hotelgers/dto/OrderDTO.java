package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.entity.Branch;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.Store;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderDTO {

    private Long orderIdx;

    private String orderCd; //임시


    private DistDTO distDTO;
    private BranchDTO branchDTO;
    private BrandDTO brandDTO;
    private StoreDTO storeDTO;


    private LocalDateTime regdate;
    private LocalDateTime moddate;



}
