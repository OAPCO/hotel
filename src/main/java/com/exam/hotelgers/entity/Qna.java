package com.exam.hotelgers.entity;


import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="qna")
@SequenceGenerator(
        name = "qna_sql",
        sequenceName = "qna_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Qna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qna_sql")
    private Long qnaIdx;


    private String qnaTitle;
    private String qnaContent;
    
    //질문자 인데그,이름
    private Long memberIdx;
    private String memberName;
    
    //0=미답변, 1=답변완료
    private int qnaStatus;
    private String qnaAnswer;


}
