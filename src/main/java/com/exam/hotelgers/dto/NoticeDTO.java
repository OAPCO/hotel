package com.exam.hotelgers.dto;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDTO {

    private Long noticeIdx;

    //유형 = 공지사항(notice),자주묻는질문(ask)로 생성시 나뉘어서 설정한다.
    private String noticeType;

    private String noticeTitle;
    private String noticeContent;


    private LocalDateTime regdate;

    private LocalDateTime moddate;

}
