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
public class ManagerDTO {

    private Long managerIdx;//메니저 키

    private String managerId;//메니저 아이디

    private String password;//메니저 비밀번호

    private Integer managerLevel;//메니저 단계

    private String managerName;//메니저 명

    private String managerPhone;//메니저 연락처

    private String managerEmail;//메니저 이메일

    private RoleType roleType;//등급

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

}
