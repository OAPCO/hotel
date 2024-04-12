package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.AnnouncementDTO;
import com.exam.hotelgers.dto.AnnouncementDTO;
import com.exam.hotelgers.entity.Announcement;
import com.exam.hotelgers.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final ModelMapper modelMapper;

    public Announcement insert(AnnouncementDTO announcementDTO) {
        Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
        Announcement result = announcementRepository.save(announcement);

        return result;
    }

    //수정
    public AnnouncementDTO update(AnnouncementDTO announcementDTO) {
        Optional<Announcement> search = announcementRepository.findById(announcementDTO.getNoticeIdx());

        if(search.isPresent()) {
            Announcement announcement = modelMapper.map(announcementDTO, Announcement.class);
            announcementRepository.save(announcement);
        }
        AnnouncementDTO result = search.map(data ->modelMapper.map(data, AnnouncementDTO.class)).orElse(null);

        return result;
    }

    //삭제
    public void delete(Long id) {
        announcementRepository.deleteById(id);
    }

    //전체조회
    public Page<AnnouncementDTO> select(Pageable page) {
        int currentPage = page.getPageNumber()-1;
        int pageLimit = 5;

        Pageable pageable = PageRequest.of(currentPage, pageLimit,
                Sort.by(Sort.Direction.DESC,"noticeIdx"));

        Page<Announcement> memberEntities = announcementRepository.findAll(pageable);


        Page<AnnouncementDTO> result = memberEntities.map(data->modelMapper.map(data,AnnouncementDTO.class));

        return result;
    }

    //개별조회
    public AnnouncementDTO read(Long id) {
        Optional<Announcement> announcement = announcementRepository.findById(id);
        //AnnouncementDTO result = modelMapper.map(announcement, AnnouncementDTO.class);
        AnnouncementDTO result = announcement.map(data->modelMapper.map(data, AnnouncementDTO.class)).orElse(null);

        return result;
    }
}
