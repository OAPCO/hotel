package com.exam.hotelgers.dto;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {

    private Long paymentIdx; //결제키

    private String offOrderSheetNo; //오프라인 주문번호

    private String paymentNo;//결제내역번호

    private int paymentPrice;//총결제금액

    private int dspositPrice;//업체입금액

    private int pgFeePrice;//pg수수료

    private int dayFeePrice;//건별 수수료

    private LocalDateTime regdate;

    private LocalDateTime moddate;
}
