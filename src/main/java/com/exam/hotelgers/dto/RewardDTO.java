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
    private String memberId; //멤버에 묶임
    private String rewardPM;
    private LocalDateTime regdate;
    private LocalDateTime moddate;

    //memberId 즉 이메일로 객체 검색 후 set해주는 방식.
}
