package com.exam.hotelgers.dto;

import com.exam.hotelgers.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesMonthDTO extends BaseEntity {
    private Long salesmonthidx;//매출키
    private String distributor_organization;//총판조직
    private String Branch;//지사
    private String store;//매장
    private Integer Total_amount;//합산금액
    private Integer Offline_payment_amount;//오프라인 결제금액
    private Integer PG_payment_amount;//PG결제금액
    private Integer PG_fee;//PG수수료
    private Integer Fee_per_case;//건별수수료
    private Integer Monthly_fee;//월정수수료
    private Integer storeremittance;//매장송금액
    private LocalDate date;//결제일자
    private LocalDate startDate;//시작날짜
    private LocalDate endDate;//종료날짜
    public static int TotalPayment(Page<SalesMonthDTO> salesMonthDTOPage) {
        List<SalesMonthDTO> salesMonthDTOList = salesMonthDTOPage.getContent();

        int totalPayment = 0;
        for (SalesMonthDTO salesMonthDTO : salesMonthDTOList) {
            if(salesMonthDTO.getTotal_amount() != null) {
                totalPayment += salesMonthDTO.getTotal_amount();
            }
        }
        return totalPayment;
    }
    public static int TotalOffline(Page<SalesMonthDTO> salesMonthDTOPage) {
        List<SalesMonthDTO> salesMonthDTOList = salesMonthDTOPage.getContent();

        int totalOffline = 0;
        for (SalesMonthDTO salesMonthDTO : salesMonthDTOList) {
            if(salesMonthDTO.getOffline_payment_amount()!= null) {
                totalOffline += salesMonthDTO.getOffline_payment_amount();
            }
        }
        return totalOffline;
    }
    public static int TotalPG_payment(Page<SalesMonthDTO> salesMonthDTOPage) {
        //페이지를 제외한 DTO 목록만 추출
        List<SalesMonthDTO> salesMonthDTOList = salesMonthDTOPage.getContent();

        int totalPG_payment = 0;
        for (SalesMonthDTO salesMonthDTO : salesMonthDTOList) {
            if(salesMonthDTO.getPG_payment_amount() != null) {
                totalPG_payment += salesMonthDTO.getPG_payment_amount();
            }
        }
        return totalPG_payment;
    }
    public static int TotalPG_fee(Page<SalesMonthDTO> salesMonthDTOPage) {
        //페이지를 제외한 DTO 목록만 추출
        List<SalesMonthDTO> salesMonthDTOList = salesMonthDTOPage.getContent();

        int totalPG_fee = 0;
        for (SalesMonthDTO salesMonthDTO : salesMonthDTOList) {
            if(salesMonthDTO.getPG_fee() != null) {
                totalPG_fee += salesMonthDTO.getPG_fee();
            }
        }
        return totalPG_fee;
    }
    public static int TotalFee_per_case(Page<SalesMonthDTO> salesMonthDTOPage) {
        //페이지를 제외한 DTO 목록만 추출
        List<SalesMonthDTO> salesMonthDTOList = salesMonthDTOPage.getContent();

        int totalFee_per_case = 0;
        for (SalesMonthDTO salesMonthDTO : salesMonthDTOList) {
            if(salesMonthDTO.getFee_per_case() != null) {
                totalFee_per_case += salesMonthDTO.getFee_per_case();
            }
        }
        return totalFee_per_case;
    }
    public static int TotalMonthly_fee(Page<SalesMonthDTO> salesMonthDTOPage) {
        //페이지를 제외한 DTO 목록만 추출
        List<SalesMonthDTO> salesMonthDTOList = salesMonthDTOPage.getContent();

        int totalMonthly_fee = 0;
        for (SalesMonthDTO salesMonthDTO : salesMonthDTOList) {
            if(salesMonthDTO.getMonthly_fee() != null) {
                totalMonthly_fee += salesMonthDTO.getMonthly_fee();
            }
        }
        return totalMonthly_fee;
    }
    public static int Totalstoreremittance(Page<SalesMonthDTO> salesMonthDTOPage) {
        //페이지를 제외한 DTO 목록만 추출
        List<SalesMonthDTO> salesMonthDTOList = salesMonthDTOPage.getContent();

        int totalstoreremittance = 0;
        for (SalesMonthDTO salesMonthDTO : salesMonthDTOList) {
            if(salesMonthDTO.getStoreremittance() != null) {
                totalstoreremittance += salesMonthDTO.getStoreremittance();
            }
        }
        return totalstoreremittance;
    }
}
