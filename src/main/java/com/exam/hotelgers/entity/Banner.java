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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "banner_sql")
    private Long bannerIdx; //광고키

    @Column(length = 200)
    private String bannerTitle; //제목
    @Column(length = 200)
    private String bannerLinkUrl;

    private LocalDateTime bannerSdate;
    private LocalDateTime bannerEdate;



    @Column(length = 50)
    private String storeCd; //제휴사 코드






}
