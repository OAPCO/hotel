package com.exam.hotelgers.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SearchDTO {
   //StoreMemberDTO
    private String storeMemberEmail;  //아이디

    private String storeMemberName; //이름

    private String storeMemberTel; //연락처

    private Integer storeMemberState; //상태

    private String storeMemberAuth; //권한

    private Integer storeDistributorIdx; //총판

    private Integer storeBranchIdx; //지사

    private Integer storeIdx; //매장
}
