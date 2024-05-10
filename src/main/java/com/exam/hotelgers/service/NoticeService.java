package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.NoticeDTO;
import com.exam.hotelgers.dto.NoticeDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import com.exam.hotelgers.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;



    public void register(NoticeDTO noticeDTO){


        Notice notice = modelMapper.map(noticeDTO, Notice.class);

        noticeRepository.save(notice);
    }





    public void delete(Long noticeIdx){

        noticeRepository.deleteById(noticeIdx);
    }



    public Page<NoticeDTO> getNoticeList(Pageable pageable, SearchDTO searchDTO) {

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"noticeIdx"));

        Page<Notice> notices = noticeRepository.selectNotice(page,searchDTO);

        Page<NoticeDTO> noticeDTOS = notices.map(data->modelMapper.map(data,NoticeDTO.class));

        return noticeDTOS;
    }



    public Page<NoticeDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"noticeIdx"));

        Page<Notice> notices = noticeRepository.findAll(page);


        Page<NoticeDTO> noticeDTOS = notices.map(data->modelMapper.map(data,NoticeDTO.class));

        return noticeDTOS;
    }





}
