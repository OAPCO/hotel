package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDTO {


        private Long bannerIdx; //광고키

        private String bannerTitle; //제목
        private String bannerLinkUrl;

        private LocalDateTime bannerSdate;
        private LocalDateTime bannerEdate;

        private String storeCd; //제휴사 코드

        private LocalDateTime regdate;
        private LocalDateTime moddate;

        private List<ImageDTO> dtoList = new ArrayList<>();
}

