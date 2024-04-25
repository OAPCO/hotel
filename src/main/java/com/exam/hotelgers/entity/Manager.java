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
@Table(name="manager")
@SequenceGenerator(
        name = "manager_sql",
        sequenceName = "manager_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Manager extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_sql")
    private Long managerIdx;//메니저 키

    @Column(length = 60, nullable = false)
    private String managerId;//메니저 아이디

    @Column(length = 100, nullable = false)
    private String password;//메니저 비밀번호

    @Column(length = 30, nullable = false)
    private String managerName;//메니저 명

    @Column(length = 50, nullable = false)
    private String managerPhone;//메니저 연락처

    @Column(length = 100, nullable = false)
    private String managerEmail;//메니저 이메일

    private String managerimgName;//메니저 이미지명



//    @OneToOne(mappedBy = "manager", cascade = CascadeType.ALL)
//    private Store store;


    @Enumerated(EnumType.STRING)
    private RoleType roleType;//등급
}
