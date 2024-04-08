package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = "banner")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image", indexes =
        {@Index(name = "idx_bannerimage_banner_bannerIdx", columnList = "banner_bannerIdx")})

public class Image extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageIdx;

    private String imageName;

    private String imageOriName;

    private String imageRepimgYn;

    @ManyToOne(fetch = FetchType.LAZY)
    private Banner banner;

    public void setbannerImg(String imageOriName,String imageName){
        this.imageOriName = imageOriName;
        this.imageName = imageName;
    }
}
