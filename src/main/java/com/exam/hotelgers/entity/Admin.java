package com.exam.hotelgers.entity;


import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="admin")
@SequenceGenerator(
        name = "admin_sql",
        sequenceName = "admin_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_sql")
    private Long adminIdx;//관리자 키

    @Column(length = 60, nullable = false)
    private String adminId;//관리자 아이디

    @Column(length = 100, nullable = false)
    private String password;//관리자 비밀번호

    @Column(length = 30, nullable = false)
    private String adminName;//관리자 이름

    @Column(length = 50, nullable = false)
    private String adminPhone;//관리자 폰 번호

    private String adminEmail;//관리자 이메일





    @Enumerated(EnumType.STRING)
    private RoleType roleType;//회원 등급
}
