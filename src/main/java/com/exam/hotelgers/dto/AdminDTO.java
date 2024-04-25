package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDTO {

    private Long adminIdx;//관리자 키

    private String adminId;//관리자 아이디

    private String password;//관리자 비밀번호

    private Integer adminLevel;//관리자 단계

    private String adminName;//관리자 이름

    private String adminPhone;//관리자 폰 번호

    private String adminEmail;//관리자 이메일

    private RoleType roleType;//회원 등급

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

}
