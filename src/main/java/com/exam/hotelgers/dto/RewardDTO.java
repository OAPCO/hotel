package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.Member;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RewardDTO {
    private Long rewardIdx;

    private String rewardName; //적립명
    private String rewardAmount; //적립액
    private Member member;
    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
