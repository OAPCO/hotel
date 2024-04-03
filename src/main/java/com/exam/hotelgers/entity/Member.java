package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//회원관리 테이블
//getter 변수별로 읽기, setter 변수별로 저장
//toString 모든변수 읽기, builder 모든변수 저장(ModelMapper 대체)
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="member")
//순차처리가 필요한경우(AutoIncrement 기능)
@SequenceGenerator(
        name = "member_sql",
        sequenceName = "member_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_sql")
    private Long memberIdx;

    @Column(length = 1)
    private Character memberJoinType; //"회원가입타입(C:일반,K:카카오톡,F:페이스북,N:네이버

    @Column(length = 1)
    private String memberType; //회원타입

    @Column(length = 200)
    private String memberEmail;//"회원 이메일

    @Column(length = 200)
    private String memberPwd;//"회원 비밀번호

    @Column(length = 200)
    private String memberNickname;//"회원 별명

    @Column(length = 13)
    private String memberPhone;//회원 전화번호

    @Column(length = 1)
    private String alramNotice;//알람수신여부(공지) : Y수신 N수신안함

    @Column(length = 1)
    private String alramBeacon;//알람수신여부(비콘) : Y수신 N수신안함

    @Column(length = 1)
    private String alramOrder;//알람수신여부(주문서) : Y수신 N수신안함

    @Column(length = 200)
    private String gcmKey;//gcm_key

    @Column(length = 1)
    private char deviceOs; //디바이스os(A:안드로이드,I:ios)

    @Column(length = 200)
    private String pwdKey; //비밀번호 재설정키


    private LocalDateTime socialInfoLoginDatetime;//Social정보(카카오톡) 로그인시간

    @Column(length = 200)
    private String socialInfoNickname;//Social정보(카카오톡) 닉네임

    @Column(length = 200)
    private String socialInfoUsername;//Social정보(카카오톡) 사용자이름

    @Column(length = 200)
    private String socialInfoEmail;//Social정보(카카오톡) 이메일

    @Column(length = 20)
    private String socialInfoAgeRange;//Social정보(카카오톡) 나이(범위)

    @Column(length = 4)
    private String socialInfoBirthYear;//Social정보(카카오톡) 출생년도

    @Column(length = 4)
    private String socialInfoBirthMmdd;//Social정보(카카오톡) 출생월일

    @Column(length = 10)
    private String socialInfoBirthType;//Social정보(카카오톡) 생일타입(양력/음력)

    @Column(length = 10)
    private String socialInfoGender;//Social정보(카카오톡) 성별




    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
