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
public class DistChiefDTO {

    private Long distChiefIdx;

    private String distChiefId;

    private String password;

    private Integer distChiefLevel;

    private String distChiefName;

    private String distChiefPhone;

    private String distChiefEmail;

    private RoleType roleType;

    private LocalDateTime regdate;

    private LocalDateTime moddate;

}
