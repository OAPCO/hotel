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
    private Long adminIdx;

    @Column(length = 60, nullable = false)
    private String adminId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String adminName;

    @Column(length = 50, nullable = false)
    private String adminPhone;


    private Integer adminLevel;



    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
