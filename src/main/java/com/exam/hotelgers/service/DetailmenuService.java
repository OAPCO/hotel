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

            if (imgFile != null && !imgFile.isEmpty()) {
                if (detailmenu.getMenuImg() != null && !detailmenu.getMenuImg().isEmpty()) {
                    s3Uploader.deleteFile(detailmenu.getMenuImg(), imgUploadLocation);
                }

                String originalFileName = imgFile.getOriginalFilename();
                String newFileName = "";

                if(originalFileName != null) {
                    newFileName = s3Uploader.upload(imgFile,imgUploadLocation);
                }

                detailmenuDTO.setMenuImg(newFileName);
                // DTO에 새 파일 이름 설정
                // 이 부분은 DTO에서 사용되는 모든 set 메서드 호출 후에 수행되어야 합니다.
            }

            modelMapper.map(detailmenuDTO, detailmenu);
            // modelMapper로 detailmenuDTO 필드를 detailmenu에 복사.

            if (imgFile != null && !imgFile.isEmpty()) {
                detailmenu.setMenuImg(detailmenuDTO.getMenuImg());
                // Map 이후 다시 temp에 menuImg 설정
                // 이 부분은 modelMapper.map(detailmenuDTO, detailmenu); 뒤에 와야 합니다.
            }

            detailmenuRepository.save(detailmenu);
        } else {
            throw new IllegalArgumentException("해당 Detailmenu가 존재하지 않습니다.");
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

