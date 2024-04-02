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
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminIdx;

    @Column(length = 60, nullable = false)
    private String adminId;

    @Column(length = 100, nullable = false)
    private String adminPwd;

    @Column(length = 30, nullable = false)
    private String adminName;

    @Column(length = 50, nullable = false)
    private String adminPhone;


    private Integer adminLevel;



    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
