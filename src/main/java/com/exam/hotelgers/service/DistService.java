package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.entity.DistChief;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.DistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
public class DistService {

    private final DistRepository distRepository;
    private final ModelMapper modelMapper;
    private final SearchService searchService;
    private final DistChiefRepository distChiefRepository;



    private DistDTO convertToDTO(Dist dist) {
        DistDTO dto = modelMapper.map(dist, DistDTO.class);
        dto.setStoreDTOList(searchService.convertToStoreDTOList(dist.getStoreList()));
        return dto;
    }

    public Long register(DistDTO distDTO) {


        Optional<DistChief> distChief = distChiefRepository.findByDistChiefName(distDTO.getDistChiefDTO().getDistChiefName());

        if (!distChief.isPresent()) {
            throw new IllegalStateException("존재하지 않는 총판장입니다.");
        }

        
        
        Optional<Dist> distEntity = distRepository.findByDistCd(distDTO.getDistCd());



        if(distEntity.isPresent()) {
            throw new IllegalStateException("이미 생성된 총판입니다.");
        }



        Dist dist = modelMapper.map(distDTO, Dist.class);


        dist.setDistChief(distChief.get());

        return distRepository.save(dist).getDistIdx();
    }


    public void modify(DistDTO distDTO){


        Optional<Dist> temp = distRepository
                .findByDistIdx(distDTO.getDistIdx());

        if(temp.isPresent()) {

            Dist dist = modelMapper.map(distDTO, Dist.class);
            distRepository.save(dist);
        }

    }

    public DistDTO read(Long distIdx){

        Optional<Dist> dist= distRepository.findById(distIdx);


        return modelMapper.map(dist, DistDTO.class);
    }



    public Page<DistDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"distIdx"));

        Page<Dist> dists = distRepository.findAll(page);


        Page<DistDTO> distDTOS = dists.map(data->modelMapper.map(data, DistDTO.class));

        return distDTOS;
    }



    public void delete(Long distIdx){
        distRepository.deleteById(distIdx);
    }





    public Page<DistDTO> searchadminstoredistmange(String distName, String distChief, Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "distIdx"));

        Page<Dist> dists = distRepository.multiSearchadminsdm(distName, distChief, page);
        return dists.map(this::convertToDistDTO);
    }

    private DistDTO convertToDistDTO(Dist dist) {
        return modelMapper.map(dist, DistDTO.class);
    }





    public Page<DistDTO> searchadmindr(String distChief, Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "distIdx"));

        Page<Dist> dists = distRepository.multiSearchadmdr(distChief, page);
        return dists.map(this::convertToDistDTO);
    }



    public Page<DistDTO> searchmemadmin(String distName,String distChiefEmail, String distChief,
                                        String distTel, Pageable pageable) {
        //유저권한,총판조직명,지사명,매장명,아이디,이름,연락처,상태
        int currentPage = pageable.getPageNumber() - 1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage, pageCnt, Sort.by(Sort.Direction.DESC, "distIdx"));

        Page<Dist> dists = distRepository.multiSearchmemadmin(distName,distChiefEmail,distChief,distTel,page);
        return dists.map(this::convertToDistDTO);
    }
}
