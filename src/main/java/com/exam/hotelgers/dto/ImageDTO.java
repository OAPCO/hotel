package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.entity.Banner;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDTO {

    private Long imageIdx;

    private String imageName;

    private String imageOriName;

    private String imageRepimgYn;

    private Banner banner;

    private LocalDateTime regdate;

    private LocalDateTime moddate;

}
