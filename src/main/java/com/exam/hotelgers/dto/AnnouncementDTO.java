package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementDTO {
    private Long noticeIdx;//공지사항 키

    private String title;//제목

    private String contents;//내용

    private String img; //이미지

    private Integer imgWidth; //이미지 가로

    private Integer imgHeight; //이미지 세로

    private String delYn;//삭제유무

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

    private String  noticeType;//공지 타입

    private Integer ref;//조회수

    private String realDelYn;//보임 안보임
}
