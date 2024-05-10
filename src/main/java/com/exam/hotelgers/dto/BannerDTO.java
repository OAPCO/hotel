package com.exam.hotelgers.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BannerDTO {


        private Long bannerIdx;

        private String bannerTitle;

        private String bannerClient;

        private String bannerClientTel;

        private String bannerPrice;

        private String bannerClickCount;

        private String bannerLinkUrl;



        private LocalDateTime regdate;
        private LocalDateTime moddate;

}

