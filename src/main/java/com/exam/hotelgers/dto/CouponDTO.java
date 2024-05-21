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
public class CouponDTO {
    private Long couponIdx; //키

    private String couponName; //쿠폰명

    private String couponPrice;//할인액

    private String couponState;//사용여부
    private String detail; //상세설명
    private String memberEmail; //멤버에 묶임
    private LocalDateTime regdate;
    private LocalDateTime moddate;


}
