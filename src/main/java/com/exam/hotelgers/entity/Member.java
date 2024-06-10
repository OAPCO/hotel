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

    private String memberName;




    private String memberZipCode;
    private String memberAddress;



    @Column(length = 1)
    private Character memberJoinType; //"회원가입타입(C:일반,K:카카오톡,F:페이스북,N:네이버


    @Column(length = 200, unique = true)
    private String memberEmail;//"회원 이메일

    @Column(length = 200)
    private String password;//"회원 비밀번호

    @Column(length = 200)
    private String kakaopassword;//"카카오 회원 비밀번호

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










    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @ToString.Exclude
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private List<Reward> rewardList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private List<Coupon> couponList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy="member", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

}
