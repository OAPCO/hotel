package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.Rate;
import com.exam.hotelgers.entity.Store;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
    private Long reviewIdx;

    private Rate rate;

    private String text;

    private Long storeIdx;

    private Long memberIdx;
}
