package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="announcement")
@SequenceGenerator(
        name = "announcement_sql",
        sequenceName = "announcement_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Announcement extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_sql")
    private Long noticeIdx;  //일련번호

    @Column(length = 200, nullable = false)
    private String title;  //제목, 생략불가능

    @Column(nullable = false)
    private String contents;//내용

    private String img; //이미지

    private Integer imgWidth; //이미지 가로

    private Integer imgHeight; //이미지 세로

    private String delYn;//삭제유무

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

    private String  noticeType;//공지타입

    private Integer ref;//조회수

    private String realDelYn;//보임 안보임

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="noticeIdx")
//    private StoreDistributorIdx storeDistributorIdx;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="noticeIdx")
//    private StoreBranchIdx storeBranchIdx;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="noticeIdx")
//    private StoreIdx storeIdx;
}
