package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.RoomType;
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

    @Column(unique = true)
    private String roomCd; //방 호수 202호 등

    private String roomName;

    //0=미예약,1=예약,2=체크인상태
    private int roomStatus;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private String roomimgName;


    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeIdx")
    private Store store;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<MenuOrder> menuOrderList = new ArrayList<>();
}

