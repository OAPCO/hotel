package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.AnnouncementDTO;
import com.exam.hotelgers.entity.Announcement;
import com.exam.hotelgers.repository.AnnouncementRepository;
import com.exam.hotelgers.repository.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final ModelMapper modelMapper;

    public Long register(AnnouncementDTO announcementMenuDTO) {


        Announcement announcement = modelMapper.map(announcementMenuDTO, Announcement.class);

        announcementRepository.save(announcement);

        return announcementRepository.save(announcement).getNoticeIdx();
    }


    public void modify(AnnouncementDTO announcementMenuDTO){




        Optional<Announcement> temp = announcementRepository
                .findByNoticeIdx(announcementMenuDTO.getTitle());


        if(temp.isPresent()) {

            Announcement announcement = modelMapper.map(announcementMenuDTO, Announcement.class);

            announcementRepository.save(announcement);
        }


    }

    public AnnouncementDTO read(Long announcementIdx){

        Optional<Announcement> announcement= announcementRepository.findById(announcementIdx);


        return modelMapper.map(announcement,AnnouncementDTO.class);
    }



    public Page<AnnouncementDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"announcementIdx"));

        Page<Announcement> announcements = announcementRepository.findAll(page);


        Page<AnnouncementDTO> announcementMenuDTOS = announcements.map(data->modelMapper.map(data,AnnouncementDTO.class));


        return announcementMenuDTOS;
    }



    public void delete(Long announcementIdx){
        announcementRepository.deleteById(announcementIdx);
    }
}
