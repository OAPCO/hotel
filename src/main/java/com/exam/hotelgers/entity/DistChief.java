package com.exam.hotelgers.entity;


import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="distchief")
@SequenceGenerator(
        name = "distchief_sql",
        sequenceName = "distchief_sql",
        initialValue = 1,
        allocationSize = 1
)
public class DistChief extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "distchief_sql")
    private Long distChiefIdx;//총판장키

    @Column(length = 60, nullable = false)
    private String distChiefId;//총판장 아이디

    @Column(length = 100, nullable = false)
    private String password;//총판장 비밀번호

    @Column(length = 30, nullable = false)
    private String distChiefName;//총판장명

    @Column(length = 50, nullable = false)
    private String distChiefPhone;//총판장 연락처

    private String distChiefEmail;//총판장 이메일

    private String distChiefimgName;//총판장 이미지명


    @OneToMany(mappedBy="distChief", cascade = CascadeType.ALL)
    private List<Dist> distList = new ArrayList<>();//총판 목록


    @Enumerated(EnumType.STRING)
    private RoleType roleType;//회원 등급
}
