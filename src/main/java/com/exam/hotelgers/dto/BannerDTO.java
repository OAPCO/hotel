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
        private String bannerLinkUrl;//광고 링크

        private LocalDateTime bannerSdate;//시작일
        private LocalDateTime bannerEdate;//종료일

        private String storeCd; //제휴사 코드

        private LocalDateTime regdate;//등록일

        private LocalDateTime moddate;//수정일

}

