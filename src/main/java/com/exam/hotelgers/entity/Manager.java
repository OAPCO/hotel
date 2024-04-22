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
    private Long managerIdx;

    @Column(length = 60, nullable = false)
    private String managerId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String managerName;

    @Column(length = 50, nullable = false)
    private String managerPhone;

    @Column(length = 100, nullable = false)
    private String managerEmail;




    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
