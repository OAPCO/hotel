package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.StoreDist;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class StoreBranchDTO {

    private Long storeBranchIdx;


    private String storeBranchId;
    private String storeBranchName;

    private String storeBranchChief;
    private String storeBranchChiefEmail;
    private String storeBranchTel;


    private StoreDistDTO storeDistDTO;

    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
