package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.PaymentDTO;

import com.exam.hotelgers.dto.PaymentDTO;
import com.exam.hotelgers.entity.Payment;
import com.exam.hotelgers.repository.PaymentRepositorty;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
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



    public void delete(Long paymentIdx){
        paymentRepository.deleteById(paymentIdx);
    }




}

