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
public class QnaDTO {

    private Long qnaIdx;


    private String qnaTitle;
    private String qnaContent;

    //질문자
    private Long memberIdx;
    private String memberName;

    //0=미답변, 1=답변완료
    private int qnaStatus;
    private String qnaAnswer;

    private LocalDateTime regdate;

    private LocalDateTime moddate;

}
