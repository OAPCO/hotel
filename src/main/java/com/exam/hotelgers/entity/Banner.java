package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="banner")
@SequenceGenerator(
        name = "banner_sql",
        sequenceName = "banner_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Banner extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Bannes_sql")
    private Long bannerIdx; //광고키

    @Column(length = 200)
    private String title; //제목
    @Column(length = 200)
    private String linkUrl;

    private LocalDateTime bannerSdate;
    private LocalDateTime bannerEdate;

    @Column(length = 200)
    private String img; //이미지경로

    @Column(length = 11)
    private int imgWidth; //이미지가로
    @Column(length = 11)
    private int imgHeight; //이미지세로


    private char state; // 0진행중 ,1종료

    @Column(length = 50)
    private String storeCd; //제휴사 코드

    private char realDelYn; // N정상 / Y삭제
    private char DelYn; // N활성화 / Y 비활성화





}
