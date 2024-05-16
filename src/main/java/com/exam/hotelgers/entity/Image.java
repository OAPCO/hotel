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
@Table(name="image")
@SequenceGenerator(
        name = "image_sql",
        sequenceName = "image_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sql")
    private Long imageIdx;

    private String imgName;

    private Long roomIdx;
    private Long bannerIdx;

    private String roomImageType;

    private int roomImageMain;



}
