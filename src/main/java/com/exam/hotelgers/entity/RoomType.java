package com.exam.hotelgers.entity;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="roomTypeType")
@SequenceGenerator(
        name = "roomTypeType_sql",
        sequenceName = "roomTypeType_sql",
        initialValue = 1,
        allocationSize = 1
)
@QueryEntity
public class RoomType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomType_sql")
    private Long roomTypeIdx;

    @Column(nullable = false)
    private String storeIdx;

    private String roomTypeName;

    private String roomTypeMainimgName;

    private String roomTypeimgName;


}

