package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="storedist")
@SequenceGenerator(
        name = "storedist_sql",
        sequenceName = "storedist_sql",
        initialValue = 1,
        allocationSize = 1
)
public class StoreDist extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storedist_sql")
    private Long storeDistIdx;


    @Column(length = 45)
    private String storeDistName;
    @Column(length = 50)
    private String storeDistChief;
    @Column(length = 200)
    private String storeDistTel;



}
