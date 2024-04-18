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

    private Long adminIdx; //키

    private String adminId;

    private String password;

    private Integer adminLevel;

    private String adminName;

    private String adminPhone;

    private RoleType roleType;

    private LocalDateTime regdate;

    private LocalDateTime moddate;

}
