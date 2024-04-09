package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString(exclude = {"banner", "store"})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image", indexes =
        {@Index(name = "idx_bannerimage_banner_bannerIdx", columnList = "banner_bannerIdx"),
                @Index(name = "idx_storeimage_store_storeIdx", columnList = "store_storeIdx")})

public class Image extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageIdx;

    private String imageName;

    private String imageOriName;

    private String imageRepimgYn;



    @ManyToOne(fetch = FetchType.LAZY)
    private Banner banner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;


}
