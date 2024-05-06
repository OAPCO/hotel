package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagerDTO {

    private Long managerIdx;
    private String managerId;
    private String password;

    private Integer managerLevel;
    private String managerName;
    private String managerPhone;
    private String managerEmail;

    private RoleType roleType;


    private LocalDateTime regdate;
    private LocalDateTime moddate;


    private List<StoreDTO> storeDTOList;

    private DistDTO distDTO;


}
