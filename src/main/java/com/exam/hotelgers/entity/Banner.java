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
    private Long bannerIdx;

    @Column
    private String bannerTitle;

    @Column
    private String bannerClient;

    @Column
    private String bannerClientTel;

    @Column
    private String bannerPrice;

    @Column
    private String bannerClickCount;

    @Column
    private String bannerLinkUrl;



}
