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
public class Room extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_sql")
    private Long roomIdx;

    @Column(unique = true)
    private String roomCd;

    private String roomName;

    @Builder.Default
    private Boolean roomStatus = false;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private String roomimgName;





    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;

    @OneToMany(mappedBy="room", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();


}
