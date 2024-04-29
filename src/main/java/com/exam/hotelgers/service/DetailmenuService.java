package com.exam.hotelgers.service;

import com.exam.hotelgers.dto.DetailmenuDTO;

import com.exam.hotelgers.dto.DetailmenuDTO;
import com.exam.hotelgers.dto.MenuOptionDTO;
import com.exam.hotelgers.entity.Detailmenu;
import com.exam.hotelgers.entity.MenuOption;
import com.exam.hotelgers.repository.DetailmenuRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//회원 가입, 수정, 삭제, 조회
@Service
@RequiredArgsConstructor
@Transactional
public class DetailmenuService {

    private final DetailmenuRepository detailmenuRepository;
    private final ModelMapper modelMapper;

    //Application.properties에 선언한 파일이 저장될 경로
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;

    //파일저장을 위한 클래스
    private final S3Uploader s3Uploader;


    public Long register(DetailmenuDTO detailmenuDTO,@RequestParam(required = false) MultipartFile imgFile) throws IOException {


        Detailmenu detailmenu = modelMapper.map(detailmenuDTO, Detailmenu.class);

        String originalFileName = imgFile.getOriginalFilename(); //저장할 파일명
        String newFileName = ""; //새로 만든 파일명

        if(originalFileName != null) { //파일이 존재하면
            newFileName = s3Uploader.upload(imgFile,imgUploadLocation);
        }

        detailmenu.setMenuImg(newFileName); //새로운 파일명을 재등록

        detailmenuRepository.save(detailmenu);

        return detailmenuRepository.save(detailmenu).getDetailmenuIdx();
    }


    public void modify(DetailmenuDTO detailmenuDTO, @Nullable MultipartFile imgFile) throws IOException {
        Optional<Detailmenu> temp = detailmenuRepository.findById(detailmenuDTO.getDetailmenuIdx());

        if(temp.isPresent()) {
            Detailmenu detailmenu = temp.get();

            // 이미지 파일 처리
            if (imgFile != null && !imgFile.isEmpty()) {
                String originalFileName = imgFile.getOriginalFilename(); // 원본 파일 이름 가져오기
                String newFileName = s3Uploader.upload(imgFile, imgUploadLocation); // imgUploadLocation으로 파일 업로드하고, 새로운 파일 이름 받아오기
                detailmenuDTO.setMenuImg(newFileName); // storeDTO에 새로운 파일 이름 설정하기
            }

            detailmenu.setMenuImg(detailmenuDTO.getMenuImg());

            modelMapper.map(detailmenuDTO, detailmenu);
            // modelMapper를 이용하여 detailmenuDTO의 필드를 detailmenu에 업데이트합니다. 업데이트된 menuImg는 자동으로 반영됩니다.

            detailmenuRepository.save(detailmenu);
        } else {
            throw new IllegalArgumentException("특정 Detailmenu는 존재하지 않습니다.");
        }
    }

    public DetailmenuDTO read(Long detailmenuIdx){

        Optional<Detailmenu> detailmenu= detailmenuRepository.findById(detailmenuIdx);


        return modelMapper.map(detailmenu,DetailmenuDTO.class);
    }



    public Page<DetailmenuDTO> list(Pageable pageable){

        int currentPage = pageable.getPageNumber()-1;
        int pageCnt = 5;
        Pageable page = PageRequest.of(currentPage,pageCnt, Sort.by(Sort.Direction.DESC,"detailmenuIdx"));

        Page<Detailmenu> detailmenu = detailmenuRepository.findAll(page);


        Page<DetailmenuDTO> detailmenuDTOS = detailmenu.map(data->modelMapper.map(data,DetailmenuDTO.class));

        return detailmenuDTOS;
    }



    public void delete(Long detailmenuIdx) throws IOException {

        //물리적위치에 저장된 이미지를 삭제
        Detailmenu detailmenu = detailmenuRepository
                .findById(detailmenuIdx)
                .orElseThrow();; //조회->저장
        //deleteFile(파일명, 폴더명)
        s3Uploader.deleteFile(detailmenu.getMenuImg(), imgUploadLocation);

        detailmenuRepository.deleteById(detailmenuIdx);

    }
}

