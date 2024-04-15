package com.exam.hotelgers.service;


import com.exam.hotelgers.dto.BannerDTO;
import com.exam.hotelgers.dto.ImageDTO;
import com.exam.hotelgers.entity.Banner;
import com.exam.hotelgers.entity.Image;
import com.exam.hotelgers.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
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
public class AnnouncementImageService {

    @Value("${imgLocation3}")
    private String imgLocation;

    public String uploadFile(String origianlFileName, MultipartFile imageFile) {
        UUID uuid = UUID.randomUUID(); //문자열생성
        String extendsion = origianlFileName.substring(origianlFileName.lastIndexOf(".")); //문자열 분리
        String saveFileName = uuid.toString()+extendsion; //새로운 파일명
        String uploadFullUrl = imgLocation+saveFileName; //저장위치 및 파일명

        //폴더확인 및 폴더 생성
        File folder = new File(imgLocation);
        if (!folder.exists()) {
            boolean result = folder.mkdirs();
        }

        try {
            byte[] filedata = imageFile.getBytes();
            //하드디스크에 파일 저장
            FileOutputStream fos = new FileOutputStream(uploadFullUrl);
            fos.write(filedata);
            fos.close();

            return saveFileName;
        } catch(Exception e) {
            return null;
        }
    }

    public void deleteFile(String fileName) {
        String deleteFileName = imgLocation+fileName;

        File deleteFile = new File(deleteFileName);
        if(deleteFile.exists()) { //파일이 존재하면
            deleteFile.delete();
        }
    }
}


