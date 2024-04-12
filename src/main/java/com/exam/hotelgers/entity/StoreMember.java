package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="storemember")
@SequenceGenerator(
        name = "storemember_seq",
        sequenceName = "storemember_seq",
        initialValue = 1,
        allocationSize = 1
)
public class StoreMember extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storemember_seq")
    private Long storeMemberIdx;

    private String storeMemberEmail;

    private String storeMemberPwd;

    private String storeMemberName;

    private String storeMemberTel;

    private Integer storeMemberState;

    private String delYn;

    private String storeMemberAuth;

    private Integer storeDistributorIdx;

    private Integer storeBranchIdx;

    private Integer storeIdx;

}
