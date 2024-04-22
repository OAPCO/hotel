package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.*;

import com.exam.hotelgers.dto.PaymentDTO;
import com.exam.hotelgers.dto.PaymentDTO;
import com.exam.hotelgers.dto.PaymentDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.entity.Payment;
import com.exam.hotelgers.entity.Payment;
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



    public Payment insert(PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        Payment result = paymentRepository.save(payment);

        return result;
    }


    public PaymentDTO update(PaymentDTO paymentDTO) {
        Optional<Payment> search = paymentRepository.findById(paymentDTO.getPaymentIdx());

        if(search.isPresent()) {
            Payment payment = modelMapper.map(paymentDTO, Payment.class);
            paymentRepository.save(payment);
        }
        PaymentDTO result = search.map(data ->modelMapper.map(data, PaymentDTO.class)).orElse(null);

        return result;
    }

    public PaymentDTO read(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        //PaymentDTO result = modelMapper.map(storeMember, PaymentDTO.class);
        PaymentDTO result = payment.map(data->modelMapper.map(data, PaymentDTO.class)).orElse(null);

        return result;
    }



    public Page<PaymentDTO> list(Pageable page) {
        int currentPage = page.getPageNumber()-1;
        int pageLimit = 5;

        Pageable pageable = PageRequest.of(currentPage, pageLimit,
                Sort.by(Sort.Direction.DESC,"paymentIdx"));

        Page<Payment> payments = paymentRepository.findAll(pageable);


        Page<PaymentDTO> result = payments.map(data->modelMapper.map(data,PaymentDTO.class));

        return result;
    }



    public void delete(Long id){
        paymentRepository.deleteById(id);
    }
}

