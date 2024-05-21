package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="reward")
@SequenceGenerator(
        name = "reward_sql",
        sequenceName = "reward_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Reward extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reward_sql")
    private Long rewardIdx;

    private String rewardName; //적립명
    private String rewardAmount; //적립액
    private String rewardPM; //사용인지 적립인지(+-)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberEmail", referencedColumnName="memberEmail")
    private Member member;
}
