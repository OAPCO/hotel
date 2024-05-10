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
@Table(name="notice")
@SequenceGenerator(
        name = "notice_sql",
        sequenceName = "notice_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_sql")
    private Long noticeIdx;

    //유형 = 공지사항(notice),자주묻는질문(ask)로 생성시 나뉘어서 설정한다.
    private String noticeType;


    @Column
    private String noticeTitle;

    @Column
    private String noticeContent;

}
