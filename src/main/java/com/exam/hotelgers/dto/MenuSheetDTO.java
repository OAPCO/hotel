package com.exam.hotelgers.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuSheetDTO {
    private Long menuSheetIdx;//메뉴 옵션키
    private Integer newOrderNo;//신규 주문번호
    private Integer menuSheetState;//주문서 상태 0.주문전, 1.조리요청, 2.결제요청, 3.결제완료, 4.결제취소, 5.조리완료, 6.배달요청, 7.배달완료
    private String menuSheetName;//주문서 이름
    private LocalDateTime orderdate;//조리 요청일
    private String orderProgressStatus;//주문상태(NEW 신규,CHECK 접수,CANCEL 취소,CALL 호출,CLOSE 완료)

    private StoreDTO storeDTO;//매장DTO
    private RoomDTO roomDTO;//룸DTO
    private LocalDateTime startDate;//시작날짜
    private LocalDateTime endDate;//종료날짜

    private LocalDateTime regdate;//등록일

    private LocalDateTime moddate;//수정일

}
