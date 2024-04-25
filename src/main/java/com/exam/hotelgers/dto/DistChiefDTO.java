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

    private Long distChiefIdx;//총판장키

    private String distChiefId;//총판장 아이디
    private String password;//총판장 비밀번호

    private Integer distChiefLevel;//총판장 단계
    private String distChiefName;//총판장 명
    private String distChiefPhone;//총판장 연락처
    private String distChiefEmail;//총판장 이메일


    private List<DistDTO> distDTOList;//총판DTO 목록


    private RoleType roleType;//등급


    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

}
