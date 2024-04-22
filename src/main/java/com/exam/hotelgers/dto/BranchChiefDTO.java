package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchChiefDTO {

    private Long branchChiefIdx;

    private String branchChiefId;

    private String password;

    private Integer branchChiefLevel;

    private String branchChiefName;

    private String branchChiefPhone;

    private String branchChiefEmail;

    private RoleType roleType;

    private LocalDateTime regdate;

    private LocalDateTime moddate;

}
