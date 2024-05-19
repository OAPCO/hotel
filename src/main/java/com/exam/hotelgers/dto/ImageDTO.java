package com.exam.hotelgers.dto;


import com.exam.hotelgers.entity.BaseEntity;
import com.exam.hotelgers.entity.Room;
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
public class ImageDTO extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_sql")
    private Long imageIdx;

    private String imgName;

    private Long bannerIdx;

    private Long roomIdx;

    private String roomImageType;

    private int roomImageMain;



}
