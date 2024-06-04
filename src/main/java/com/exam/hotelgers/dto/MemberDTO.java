package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.entity.Coupon;
import com.exam.hotelgers.entity.Reward;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberDTO {

    private Long memberIdx;

    private Character memberJoinType; //"회원가입타입(C:일반,K:카카오톡,F:페이스북,N:네이버

    private String memberType; //회원타입

    private String memberEmail;//"회원 이메일

    private String password;//"회원 비밀번호
    private String kakaopassword;//"회원 비밀번호

    private String memberNickname;//"회원 별명

    private String memberId;
    private String memberName;

    private String memberZipCode;
    private String memberAddress;


    private String memberPhone;//회원 전화번호

    private String alramNotice;//알람수신여부(공지) : Y수신 N수신안함

    private String alramBeacon;//알람수신여부(비콘) : Y수신 N수신안함

    private String alramOrder;//알람수신여부(주문서) : Y수신 N수신안함



    private LocalDateTime regdate;

    private LocalDateTime moddate;


    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private List<Reward> rewardList = new ArrayList<>();

    private List<Coupon> couponList = new ArrayList<>();
}
