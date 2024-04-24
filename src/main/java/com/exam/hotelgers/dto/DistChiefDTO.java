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
public class DistChiefDTO {

    private Long distChiefIdx;

    private String distChiefId;
    private String password;

    private Integer distChiefLevel;
    private String distChiefName;
    private String distChiefPhone;
    private String distChiefEmail;


//    private List<DistDTO> distDTOList;


    private RoleType roleType;


    private LocalDateTime regdate;
    private LocalDateTime moddate;

}
