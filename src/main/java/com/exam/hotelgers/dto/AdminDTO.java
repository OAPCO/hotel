package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.entity.Branch;
import com.exam.hotelgers.entity.Dist;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDTO {

    private Long adminIdx;

    private String adminId;

    private String password;

    private Integer adminLevel;

    private String adminName;

    private String adminPhone;

    private String adminEmail;

    private RoleType roleType;

    private LocalDateTime regdate;

    private LocalDateTime moddate;


    private List<Dist> distList = new ArrayList<>();

    private List<Branch> branchList = new ArrayList<>();

}
