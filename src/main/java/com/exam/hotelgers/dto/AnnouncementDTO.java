package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnnouncementDTO {
    private Long noticeIdx;

    private String title;

    private String contents;

    private String img;

    private Integer imgWidth;

    private Integer imgHeight;

    private String  noticeType;

    private Integer ref;
}
