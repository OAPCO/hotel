package com.exam.hotelgers.entity;

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
@Table(name="detailmenu")
@SequenceGenerator(
        name = "detailmenu_sql",
        sequenceName = "detailmenu_sql",
        initialValue = 1,
        allocationSize = 1
)
public class Detailmenu extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detailmnu_sql")
    private Long detailmenuIdx; //메뉴키

    @Column(length = 200, nullable = false)
    private String detailMenuName; //메뉴명
    @Column(length = 2)
    private String optionYn; //옵션여부 n
    @Column(length = 200)
    private String menuImg; //메뉴 이미지
    @Column(length = 500)
    private String menuDescription; //메뉴설명



    //    @Column(length = 100)
    private char recommendYn; //추천메뉴
    private char salesYn; //판매여부 Y:판매중 N:품절
    private char deliveryYn; //배달여부 Y:배달가능 N:배달불가
    @Column(length = 30)
    private String menuSaleType; //할인구분 NoNE:없음, AMOUNT:
    @Column(length = 11)
    private int menuSaleAmount; //할인액
    @Column(length = 11)
    private int menuSalePercent; //할인율
    @Column(length = 30)
    private String pointSaveType; //포인트 적립구분
    @Column(length = 11)
    private int pointSaveAmount; //포인트 적립액
    @Column(length = 11)
    private int pointSavePercent; //todo 포인트 적립율

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menuCateIdx")
    private MenuCate menuCate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="storeIdx")
    private Store store;

    @OneToMany(mappedBy="detailmenu", cascade = CascadeType.ALL)
    private List<MenuOption> menuOptionList = new ArrayList<>();


    public void setMenuOptions(List<MenuOption> menuOptions) {
        this.menuOptionList = menuOptions;
        menuOptions.forEach(option -> option.setDetailmenu(this));
    }


    // 옵션 테이블 활용 포기 그냥 옵션과 가격을 여기에 포함
    private int menuprice; //메뉴 가격
    private String optionname; //옵션 이름
}
