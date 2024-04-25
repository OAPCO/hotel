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
    private Long roomIdx;//룸 키

    @Column(unique = true)
    private String roomCd;//룸 코드(번호)

    private String roomName;//룸명

    private String roomimgName;//룸이미지 명





    @Enumerated(EnumType.STRING)
    private RoomType roomType;//룸 종류



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;//매장

    @OneToMany(mappedBy="room", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();//주문 목록




}
