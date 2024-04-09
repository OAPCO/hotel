package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

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
public class Room extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_sql")
    private Long roomIdx; //매장룸키
    @Column(length = 20)
    private String roomType;//룸구분
    @Column(length = 200)
    private String storeCD;//매장코드
    @Column(length = 200)
    private String qrCode;//룸 qr코드
    @Column(length = 200)
    private String roomNo;//룸 번호
    @Column(length = 50)
    private String roomName;//룸 이름
    @Column(length = 11)
    private long orderNo;//정렬 순서
    @Column(insertable = false, updatable = false)
    private Long storeIdx;//매장식별키


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;
}
