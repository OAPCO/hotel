package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.ImageDTO;
import com.exam.hotelgers.dto.QnaDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.Admin;
import com.exam.hotelgers.entity.Image;
import com.exam.hotelgers.entity.Qna;
import com.exam.hotelgers.repository.ImageRepository;
import com.exam.hotelgers.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class QnaService {


    private final QnaRepository qnaRepository;
    private final ModelMapper modelMapper;
    


    //문의 생성
    public void register(QnaDTO qnaDTO){


        Qna qna = modelMapper.map(qnaDTO, Qna.class);

        //문의상태=0=답변대기
        qna.setQnaStatus(0);

        qnaRepository.save(qna);

    }




    //일부 회원의 문의 불러오기
    public Page<QnaDTO> getQna(Pageable pageable, SearchDTO searchDTO) {

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"qnaIdx"));

        Page<Qna> qnas = qnaRepository.selectQna(page,searchDTO);

        Page<QnaDTO> qnaDTOS = qnas.map(data->modelMapper.map(data,QnaDTO.class));

        return qnaDTOS;
    }



    //모든 문의 불러오기
    public Page<QnaDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"qnaIdx"));

        Page<Qna> qnas = qnaRepository.findAll(page);


        Page<QnaDTO> qnaDTOS = qnas.map(data->modelMapper.map(data,QnaDTO.class));

        return qnaDTOS;
    }

}
