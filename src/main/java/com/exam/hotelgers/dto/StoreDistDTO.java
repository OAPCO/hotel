package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class StoreDistDTO {

    private Long storeDistIdx;


    private String storeDistName;
    private String storeDistChief;
    private String storeDistTel;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
