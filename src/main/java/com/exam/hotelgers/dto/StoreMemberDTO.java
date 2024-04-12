package com.exam.hotelgers.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class StoreMemberDTO {
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
