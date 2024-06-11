package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.AdminIncomeDTO;
import com.exam.hotelgers.dto.PaymentDTO;

import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Payment;
import com.exam.hotelgers.entity.Room;
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

import java.security.Principal;
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
    private final AdminIncomeService adminIncomeService;



    public Long register(PaymentDTO paymentDTO) {

        //수수료 내기 전 금액
        int totalPrice = paymentDTO.getPaymentPrice();

        //중계수수료 5퍼 내야함
        int charge = (int) (paymentDTO.getPaymentPrice() * 0.05);
        paymentDTO.setPaymentTotalPrice(totalPrice-charge);


        Payment payment = modelMapper.map(paymentDTO, Payment.class);

        Long paymentIdx = paymentRepository.save(payment).getPaymentIdx();


        //수수료와 가진 정보들을 DTO에 담아서 수수료컬럼 만들자

        //기존에 payment가 있을 경우 register 하는 것 = 당일 취소
        if (paymentDTO.getPaymentIdx()!=null){

            adminIncomeService.incomePriceHalfModify(paymentDTO.getPaymentIdx());
        }

        //기존에 payment가 없을 경우 = 새로 인컴 생성
        else {
            AdminIncomeDTO adminIncomeDTO = new AdminIncomeDTO();

            //필요컬럼 : 수수료금액,타입,매장idx,총판idx
            adminIncomeDTO.setIncomePrice(charge);
            adminIncomeDTO.setStoreIdx(paymentDTO.getStoreIdx());
            adminIncomeDTO.setDistIdx(paymentDTO.getDistIdx());
            adminIncomeDTO.setIncomeType(paymentDTO.getPaymentType());
            adminIncomeDTO.setPaymentIdx(paymentIdx);

            log.info("세팅된 수수료"+adminIncomeDTO);
            //수수료칼럼 생성
            adminIncomeService.register(adminIncomeDTO);
        }




        return paymentIdx;
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




    //소유한 모든 총판의 결제내역 리스트
    public Page<PaymentDTO> distPaymentlist(Pageable pageable, Principal principal){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"paymentIdx"));

        Page<Payment> payments = paymentRepository.distChiefPaymentSearch(page,principal.getName());


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


    @Transactional
    public void paymentCancel(Long roomorderIdx) {
        paymentRepository.paymentCancel(roomorderIdx);
    }

    @Transactional
    public void paymentCancelCharge(Long roomorderIdx) {
        paymentRepository.paymentCancelCharge(roomorderIdx);
    }


    public PaymentDTO roomOrderByPaymentSearch(Long paymentorderIdx) {


        Optional<Payment> payment = paymentRepository.roomOrderByPaymentSearch(paymentorderIdx);


        PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);

        return paymentDTO;
    }




}

