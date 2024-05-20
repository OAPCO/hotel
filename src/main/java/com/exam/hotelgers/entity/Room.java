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
@Table(name="room")
@SequenceGenerator(
        name = "room_sql",
        sequenceName = "room_sql",
        initialValue = 1,
        allocationSize = 1
)
@QueryEntity
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_sql")
    private Long roomIdx;

    private String roomCd; //방 호수 202호 등

    private String roomName;

    private int roomStatus;
    private int roomPrice;

    private String roomType;

    private String roomMainimgName;

    private String roomimgName;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeIdx")
    private Store store;


    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<MenuOrder> menuOrderList = new ArrayList<>();
}

