package com.exam.hotelgers.service;


import com.exam.hotelgers.dto.BannerDTO;
import com.exam.hotelgers.dto.ImageDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Banner;
import com.exam.hotelgers.entity.Image;
import com.exam.hotelgers.entity.Store;
import com.exam.hotelgers.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ImageService {


    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Value("C:/uploads/")
    private String uploadPath;

    public String makeDir(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String now = sdf.format(date);

        String path = uploadPath + "\\\\" + now;

        File file = new File(path);
        if(file.exists() == false){
            file.mkdir();
        }

        return path;
    }


    public void saveBannerImg(MultipartFile file, BannerDTO bannerDTO){

        String filePath = makeDir();
        String uuid = UUID.randomUUID().toString();

        String newFileName = uuid + "_"+
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("\\")+1);


        Image image = Image.builder().imageOriName(file.getOriginalFilename()).
                banner(modelMapper.map(bannerDTO, Banner.class)).
                imageName(newFileName).
                imageRepimgYn("Y").build();

        imageRepository.save(image);


        String fileUploadFullUrl = filePath + "\\" + newFileName;

        File save = new File(fileUploadFullUrl);

        try {
            file.transferTo(save);
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public void saveStoreImg(MultipartFile file, StoreDTO storeDTO){

        String filePath = makeDir();
        String uuid = UUID.randomUUID().toString();

        String newFileName = uuid + "_"+
                file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("\\")+1);


        Image image = Image.builder().imageOriName(file.getOriginalFilename()).
                store(modelMapper.map(storeDTO, Store.class)).
                imageName(newFileName).
                imageRepimgYn("Y").build();

        imageRepository.save(image);


        String fileUploadFullUrl = filePath + "\\" + newFileName;

        File save = new File(fileUploadFullUrl);

        try {
            file.transferTo(save);
        }catch (IOException e){
            e.printStackTrace();
        }

    }




    public List<ImageDTO> bannerimgList(Long bannerIdx) {

        List<Image> bannerImageList =
                imageRepository.findByBanner_BannerIdxOrderByImageIdxAsc(bannerIdx);


        List<ImageDTO> ImageDTOList = new ArrayList<>();



        for (int i = 0; i < bannerImageList.size(); i++) {
            ImageDTO imageDTO = new ImageDTO();

            imageDTO.setImageName(bannerImageList.get(i).getImageName());
            imageDTO.setImageOriName(bannerImageList.get(i).getImageOriName());
            imageDTO.setImageIdx(bannerImageList.get(i).getImageIdx());
            imageDTO.setImageRepimgYn(bannerImageList.get(i).getImageRepimgYn());
            imageDTO.setRegdate(bannerImageList.get(i).getRegdate());
            imageDTO.setModdate(bannerImageList.get(i).getModdate());

            ImageDTOList.add(imageDTO);
        }



        return ImageDTOList;
    }




    public List<ImageDTO> storeimgList(Long storeIdx) {

        List<Image> storeImageList =
                imageRepository.findByStore_StoreIdxOrderByImageIdxAsc(storeIdx);


        List<ImageDTO> ImageDTOList = new ArrayList<>();



        for (int i = 0; i < storeImageList.size(); i++) {
            ImageDTO imageDTO = new ImageDTO();

            imageDTO.setImageName(storeImageList.get(i).getImageName());
            imageDTO.setImageOriName(storeImageList.get(i).getImageOriName());
            imageDTO.setImageIdx(storeImageList.get(i).getImageIdx());
            imageDTO.setImageRepimgYn(storeImageList.get(i).getImageRepimgYn());
            imageDTO.setRegdate(storeImageList.get(i).getRegdate());
            imageDTO.setModdate(storeImageList.get(i).getModdate());

            ImageDTOList.add(imageDTO);
        }


        return ImageDTOList;
    }





    public Long remove(Long imageIdx){
        System.out.println("삭제"+imageIdx);
        return imageRepository.deleteImageByImageIdx(imageIdx);
    }
}


