package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDTO {


        private Long bannerIdx; //광고키
        private String title; //제목
        private String linkUrl;
        private LocalDateTime bannerSdate; //todo string으로 바꿀수있음
        private LocalDateTime bannerEdate; //todo string으로 바꿀수있음
        private String img; //이미지경로
        private int imgWidth; //이미지가로
        private int imgHeight; //이미지세로
        private char state; // 0진행중 ,1종료
        private String storeCd; //제휴사 코드
        private char realDelYn; // N정상 / Y삭제
        private char DelYn; // N활성화 / Y 비활성화

        private LocalDateTime regdate;

        private LocalDateTime moddate;
}

