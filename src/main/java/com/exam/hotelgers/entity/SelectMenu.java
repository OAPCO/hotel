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
@Table(name="selectmenu")
@SequenceGenerator(
        name = "selectmenu_sql",
        sequenceName = "selectmenu_sql",
        initialValue = 1,
        allocationSize = 1
)
public class SelectMenu extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "selectmenu_sql")
    private Long selectMenuIdx;

    @Column(length = 200, nullable = true)
    private String menuSheetNo;

    @Column(length = 11, nullable = true)
    private Integer menuOptionIdx;

    @Column(length = 11, nullable = true)
    private Integer menuPrice;

    @Column(length = 11, nullable = true)
    private Integer memberIdx;

//    @Column(length = 11, nullable = true)
//    private Integer tblMenuSheetMenuSheetldx1;
}
