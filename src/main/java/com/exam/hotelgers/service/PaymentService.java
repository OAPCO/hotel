package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.PaymentDTO;

import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Payment;
import com.exam.hotelgers.repository.PaymentRepositorty;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class PaymentService {

    private final PaymentRepositorty paymentRepository;
    private final ModelMapper modelMapper;



    public Long register(PaymentDTO paymentDTO) {


        Payment payment = modelMapper.map(paymentDTO, Payment.class);

        paymentRepository.save(payment);

        return paymentRepository.save(payment).getPaymentIdx();
    }


    public void modify(PaymentDTO paymentDTO){



        Payment payment = modelMapper.map(paymentDTO, Payment.class);

        paymentRepository.save(payment);

    }

    public PaymentDTO read(Long paymentIdx){

        Optional<Payment> payment= paymentRepository.findById(paymentIdx);


        return modelMapper.map(payment,PaymentDTO.class);
    }



    //매장의 결제내역 리스트
    public Page<PaymentDTO> list(Pageable pageable,Long storeIdx){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"paymentIdx"));

        Page<Payment> payments = paymentRepository.storesalesSearch(page,storeIdx);


        Page<PaymentDTO> paymentDTOS = payments.map(data->modelMapper.map(data,PaymentDTO.class));

        return paymentDTOS;
    }


    public Page<PaymentDTO> searchList(Pageable pageable, SearchDTO searchDTO){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"paymentIdx"));

        Page<Payment> payments = paymentRepository.storesalesDateSearch(page,searchDTO);


        Page<PaymentDTO> paymentDTOS = payments.map(data->modelMapper.map(data,PaymentDTO.class));

        return paymentDTOS;
    }



    public void delete(Long paymentIdx){
        paymentRepository.deleteById(paymentIdx);
    }


    
    
    //속한 매장의 매출반환 구역
    //각 상황별 매출 반환
    public Object[][] getYearlySales(Long storeIdx) {

        List<Object[]> yearSales = paymentRepository.getYearSales(storeIdx);

        Object[][] yearSalesArray = new Object[yearSales.size()][];

        for (int i = 0; i < yearSales.size(); i++) {
            yearSalesArray[i] = yearSales.get(i);
        }

        log.info("찍어보기"+yearSalesArray[0][0]);

        return yearSalesArray;
    }


    public Object[][] getMonthSales(Long storeIdx) {

        List<Object[]> monthSales = paymentRepository.getMonthSales(storeIdx);

        Object[][] monthSalesArray = new Object[monthSales.size()][];

        for (int i = 0; i < monthSales.size(); i++) {
            monthSalesArray[i] = monthSales.get(i);
        }

        log.info("찍어보기"+monthSalesArray[0][0]);
        log.info("찍어보기"+monthSalesArray[0][1]);

        return monthSalesArray;
    }

    public Object[][] getDaySales(Long storeIdx) {

        List<Object[]> daySales = paymentRepository.getDaySales(storeIdx);

        Object[][] daySalesArray = new Object[daySales.size()][];

        for (int i = 0; i < daySales.size(); i++) {
            daySalesArray[i] = daySales.get(i);
        }

        log.info("찍어보기"+daySalesArray[0][0]);
        log.info("찍어보기"+daySalesArray[0][1]);

        return daySalesArray;
    }






    //속한 총판의 매출 구역
    //각 상황별 매출 반환
    public Object[][] getDistYearlySales(Long distIdx) {

        List<Object[]> yearSales = paymentRepository.getDistYearSales(distIdx);

        Object[][] yearSalesArray = new Object[yearSales.size()][];

        for (int i = 0; i < yearSales.size(); i++) {
            yearSalesArray[i] = yearSales.get(i);
        }

        log.info("찍어보기"+yearSalesArray[0][0]);

        return yearSalesArray;
    }


    public Object[][] getDistMonthSales(Long distIdx) {

        List<Object[]> monthSales = paymentRepository.getDistMonthSales(distIdx);

        Object[][] monthSalesArray = new Object[monthSales.size()][];

        for (int i = 0; i < monthSales.size(); i++) {
            monthSalesArray[i] = monthSales.get(i);
        }

        log.info("찍어보기"+monthSalesArray[0][0]);
        log.info("찍어보기"+monthSalesArray[0][1]);

        return monthSalesArray;
    }

    public Object[][] getDistDaySales(Long distIdx) {

        List<Object[]> daySales = paymentRepository.getDistDaySales(distIdx);

        Object[][] daySalesArray = new Object[daySales.size()][];

        for (int i = 0; i < daySales.size(); i++) {
            daySalesArray[i] = daySales.get(i);
        }

        log.info("찍어보기"+daySalesArray[0][0]);
        log.info("찍어보기"+daySalesArray[0][1]);

        return daySalesArray;
    }


    public Object[][] getDistChiefYearSales(Long distChiefIdx) {

        List<Object[]> yearSales = paymentRepository.getDistChiefYearSales(distChiefIdx);

        Object[][] yearSalesArray = new Object[yearSales.size()][];

        for (int i = 0; i < yearSales.size(); i++) {
            yearSalesArray[i] = yearSales.get(i);
        }

        log.info("찍어보기1234"+yearSalesArray[0][0]);
        log.info("찍어보기1234"+yearSalesArray[0][1]);

        return yearSalesArray;
    }


    public Object[][] getDistChiefMonthSales(Long distChiefIdx) {

        List<Object[]> monthSales = paymentRepository.getDistChiefMonthSales(distChiefIdx);

        Object[][] monthSalesArray = new Object[monthSales.size()][];

        for (int i = 0; i < monthSales.size(); i++) {
            monthSalesArray[i] = monthSales.get(i);
        }

        log.info("찍어보기1234"+monthSalesArray[0][0]);
        log.info("찍어보기1234"+monthSalesArray[0][1]);

        return monthSalesArray;
    }


    public Object[][] getDistChiefDaySales(Long distChiefIdx) {

        List<Object[]> daySales = paymentRepository.getDistChiefDaySales(distChiefIdx);

        Object[][] daySalesArray = new Object[daySales.size()][];

        for (int i = 0; i < daySales.size(); i++) {
            daySalesArray[i] = daySales.get(i);
        }

        log.info("찍어보기1234"+daySalesArray[0][0]);
        log.info("찍어보기1234"+daySalesArray[0][1]);

        return daySalesArray;
    }




}

