package com.exam.hotelgers.service;


import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.ImageDTO;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.AdminRepository;
import com.exam.hotelgers.repository.DistChiefRepository;
import com.exam.hotelgers.repository.ImageRepository;
import com.exam.hotelgers.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class ImageService {


    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;


    @Value("${imgUploadLocation}")
    private String imgUploadLocation;
    private final S3Uploader s3Uploader;



    public void bannerImageregister(List<MultipartFile> imgFiles, Long bannerIdx) throws IOException {
        // 받아온 이미지 파일들을 처리
        for (MultipartFile imgFile : imgFiles) {
            String originalFileName = imgFile.getOriginalFilename();
            String newFileName = "";

            if (originalFileName != null) {
                // 이미지를 업로드하고 새 파일 이름을 받아옴
                newFileName = s3Uploader.upload(imgFile, imgUploadLocation);
            }

            // 새 파일 이름으로 이미지 엔티티 생성
            Image image = new Image();
            image.setImgName(newFileName);
            image.setBannerIdx(bannerIdx);

            // 이미지 저장
            imageRepository.save(image);
        }
    }








    //객실 세부 이미지 생성
    public void roomImageregister(List<MultipartFile> imgFiles, Long roomIdx, String roomType) throws IOException {

        for (MultipartFile imgFile : imgFiles) {
            String originalFileName = imgFile.getOriginalFilename();
            String newFileName = "";

            if (originalFileName != null) {
                newFileName = s3Uploader.upload(imgFile, imgUploadLocation);
            }

            Image image = new Image();
            image.setImgName(newFileName);
            image.setRoomIdx(roomIdx);
            image.setRoomImageType(roomType);
            image.setRoomImageMain(0);

            imageRepository.save(image);
        }
    }




    //객실 대표이미지 생성
    public String roomMainImageregister(MultipartFile mainimgFile, Long roomIdx, String roomType) throws IOException {

            String originalFileName = mainimgFile.getOriginalFilename();
            String newFileName = "";

            if (originalFileName != null) {
                newFileName = s3Uploader.upload(mainimgFile, imgUploadLocation);
            }

            Image image = new Image();
            image.setImgName(newFileName);
            image.setRoomIdx(roomIdx);
            image.setRoomImageType(roomType);
            image.setRoomImageMain(1);

            imageRepository.save(image);

            return newFileName;
        }




    public String roomMainImageregister2(MultipartFile mainimgFile, Long roomIdx, String roomType) throws IOException {

        String originalFileName = mainimgFile.getOriginalFilename();
        String newFileName = "";

        if (originalFileName != null) {
            newFileName = s3Uploader.upload(mainimgFile, imgUploadLocation);
        }

        Image image = new Image();
        image.setImgName(newFileName);
        image.setRoomIdx(roomIdx);
        image.setRoomImageType(roomType);
        image.setRoomImageMain(1);

        imageRepository.save(image);

        return newFileName;
    }






    public List<ImageDTO> getBannerImages(Long bannerIdx) {
        List<Image> images = imageRepository.bannerImageList(bannerIdx);
        // 이미지 엔티티를 이미지 DTO로 변환
        return images.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());
    }


    public void delete(Long imageIdx){
        imageRepository.deleteById(imageIdx);
    }


    public List<ImageDTO> roomImageSearch(Long StoreIdx) {


        List<Image> images = imageRepository.roomDetailImageSearch(StoreIdx);


        List<ImageDTO> imageDTOS = images.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());

        return imageDTOS;
    }



    public List<ImageDTO> roomMainImageSearch(Long StoreIdx) {


        List<Image> images = imageRepository.roomDetailMainImageSearch(StoreIdx);


        List<ImageDTO> imageDTOS = images.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());

        return imageDTOS;
    }



    public ImageDTO roomTypeMainImageSearch(SearchDTO searchDTO) {


        Optional<Image> image = imageRepository.roomTypeMainImageSearch(searchDTO);


        ImageDTO imageDTO = modelMapper.map(image,ImageDTO.class);

        return imageDTO;
    }


    public List<ImageDTO> roomTypeDetailImageSearch(SearchDTO searchDTO) {


        List<Image> images = imageRepository.roomTypeDetailMainImageSearch(searchDTO);


        List<ImageDTO> imageDTOS = images.stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());

        return imageDTOS;
    }




}
