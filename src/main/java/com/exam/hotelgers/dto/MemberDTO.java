package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
public class MemberDTO {

    private Long memberIdx;

    private Character memberJoinType; //"회원가입타입(C:일반,K:카카오톡,F:페이스북,N:네이버

    private String memberType; //회원타입

    private String memberEmail;//"회원 이메일

    private String password;//"회원 비밀번호

    private String memberNickname;//"회원 별명

    private String memberPhone;//회원 전화번호

    private String alramNotice;//알람수신여부(공지) : Y수신 N수신안함

    private String alramBeacon;//알람수신여부(비콘) : Y수신 N수신안함

    private String alramOrder;//알람수신여부(주문서) : Y수신 N수신안함

    private String gcmKey;//gcm_key

    private char deviceOs; //디바이스os(A:안드로이드,I:ios)

    private String pwdKey; //비밀번호 재설정키


    private LocalDateTime socialInfoLoginDatetime;//Social정보(카카오톡) 로그인시간

    private String socialInfoNickname;//Social정보(카카오톡) 닉네임

    private String socialInfoUsername;//Social정보(카카오톡) 사용자이름

    private String socialInfoEmail;//Social정보(카카오톡) 이메일

    private String socialInfoAgeRange;//Social정보(카카오톡) 나이(범위)

    private String socialInfoBirthYear;//Social정보(카카오톡) 출생년도

    private String socialInfoBirthMmdd;//Social정보(카카오톡) 출생월일

    private String socialInfoBirthType;//Social정보(카카오톡) 생일타입(양력/음력)

    private String socialInfoGender;//Social정보(카카오톡) 성별

    private LocalDateTime regdate;

    private LocalDateTime moddate;


    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
