package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="roomorder")
@SequenceGenerator(
        name = "roomorder_sql",
        sequenceName = "roomorder_sql",
        initialValue = 1,
        allocationSize = 1
)
public class RoomOrder extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roomorder_sql")
    private Long roomorderIdx;

    private String userName;
    private String userPhone;
    private String startTime;
    private String endTime;







    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="roomCd")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;

}
