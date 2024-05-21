package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="member")
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

    private String memberId;
    private String memberName;

    //주민번호
    private String memberNum;

    //프로필사진
    private String memberimgName;

    private String memberZipCode;
    private String memberAddress;



    @Column(length = 1)
    private Character memberJoinType; //"회원가입타입(C:일반,K:카카오톡,F:페이스북,N:네이버

    @Column(length = 1)
    private String memberType; //회원타입

    @Column(length = 200)
    private String memberEmail;//"회원 이메일

    @Column(length = 200)
    private String password;//"회원 비밀번호

    @Column(length = 200)
    private String kakaopassword;//"회원 비밀번호

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

    @ToString.Exclude
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private List<Reward> rewardList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private List<Coupon> couponList = new ArrayList<>();
}
