package com.exam.hotelgers.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="announcement")
@SequenceGenerator(
        name = "announcement_sql",
        sequenceName = "announcement_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Announcement extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_sql")
    private Long noticeIdx;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(length = 200, nullable = false)
    private String img;

    @Column(length = 11, nullable = false)
    private Integer imgWidth;

    @Column(length = 11, nullable = false)
    private Integer imgHeight;

    @Column(length = 1, nullable = false)
    private String  noticeType;

    @Column(length = 11, nullable = false)
    private Integer ref;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="noticeIdx")
//    private StoreDistributorIdx storeDistributorIdx;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="noticeIdx")
//    private StoreBranchIdx storeBranchIdx;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="noticeIdx")
//    private StoreIdx storeIdx;
}
