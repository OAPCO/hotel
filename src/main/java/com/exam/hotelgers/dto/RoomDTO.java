package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDTO {
    private Long roomIdx; //매장룸키
    private String roomType;//룸구분
    private String storeCD;//매장코드
    private String qrCode;//룸 qr코드
    private String roomNo;//룸 번호
    private String roomName;//룸 이름
    private long orderNo;//정렬 순서
    private Long storeIdx;//매장식별키
    private LocalDateTime regdate;
    private LocalDateTime moddate;

    private StoreDTO storeDTO;
}
