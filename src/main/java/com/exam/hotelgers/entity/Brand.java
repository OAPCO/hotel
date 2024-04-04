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
@Table(name="brand")
@SequenceGenerator(
        name = "brand_sql",
        sequenceName = "brandsql",
        initialValue = 1,
        allocationSize = 1
)
public class Brand extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_sql")
    private Long brandIdx; //브랜드키



    @Column(nullable=true, length=200)
    private String brandName; //브랜드 이름
    
    @Column(nullable=true, length=2)
    private String brandCode; //브랜드 코드
    
    @Column(nullable=true, length=45)
    private String brandOpenTime; //브랜드 오픈 시간
    
    @Column(nullable=true, length=45)
    private String brandCloseTime; //브랜드 종료 시간
    


//    @Column(nullable=false, length=11)
//    private Long storeDistIdx; //매장총판키(호텔키)
//    @Column(nullable=true, length=45)
//    private String storeDistId; //총판ID(호텔ID)

//    @Column(nullable=true, length=11)
//    private Long tblStoreMemberStoreMemberIdx; //매장회원 키

//    @Column(nullable=true, length=11)
//    private Long storeMemberIdx; //매장회원키
//
//    @Column(nullable=true, length=11)
//    private Long storeCateIdx; //매장 카테고리키


}
