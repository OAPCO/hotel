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

    private Long paymentIdx;

    private int paymentPrice;

    private String paymentType;

    private LocalDateTime regdate;

    private LocalDateTime moddate;
}
