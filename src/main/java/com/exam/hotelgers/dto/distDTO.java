package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class distDTO {

    private Long distIdx;

    private String distCd;


    private String distName;
    private String distChief;
    private String distTel;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
