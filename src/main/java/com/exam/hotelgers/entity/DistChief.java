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
    private Long distChiefIdx;

    @Column(length = 60, nullable = false)
    private String distChiefId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String distChiefName;

    @Column(length = 50, nullable = false)
    private String distChiefPhone;

    private String distChiefEmail;

    private String distChiefimgName;


    @ToString.Exclude
    @OneToMany(mappedBy = "distChief", cascade = CascadeType.ALL)
    private List<Dist> distList = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
