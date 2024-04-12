package com.exam.hotelgers.entity;

import com.exam.hotelgers.constant.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name="menucate")
@SequenceGenerator(
        name = "menucate_sql",
        sequenceName = "menucate_sql",
        initialValue = 1,
        allocationSize = 1
)
public class MenuCate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menucate_sql")
    private Long menuCateIdx;

    @Column(length = 200)
    private String menuCateName; //메뉴 카테고리 이름

    @Column(length = 200)
    private String tblStoreStoreIdx;//매장 식별키


}
