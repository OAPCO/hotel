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
@Table(name="branchChief")
@SequenceGenerator(
        name = "branchChief_sql",
        sequenceName = "branchChief_sql",
        initialValue = 1,
        allocationSize = 1
)
public class BranchChief extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branchChief_sql")
    private Long branchChiefIdx;

    @Column(length = 60, nullable = false)
    private String branchChiefId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String branchChiefName;

    @Column(length = 50, nullable = false)
    private String branchChiefPhone;

    private String branchChiefEmail;





    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
