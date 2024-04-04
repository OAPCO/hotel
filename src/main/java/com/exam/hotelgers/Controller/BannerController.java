package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.BannerDTO;
import com.exam.hotelgers.dto.ImageDTO;
import com.exam.hotelgers.service.BannerService;
import com.exam.hotelgers.service.ImageService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BannerController {


    private final BannerService bannerService;
    private final ImageService imageService;

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


    @GetMapping("/banner/register")
    public String register() {
        return "banner/register";
    }


    @PostMapping("/banner/register")
    public String registerProc(@Valid BannerDTO bannerDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @RequestParam("file") MultipartFile file){

        log.info("banner registerProc 도착" + bannerDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error 발생");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }





        //파일삭제

//        if(imageIdx != null){
//
//            for(int i=0;i<imageIdx.length;i++){
//
//                if(imageIdx[i] != null) {
//
//                    log.info(imageService.remove(imageIdx[i]));
//                }
//            }
//        }
//
//
//
//
//        //파일저장
//        if(file != null){
//
//            for(int i=0; i< file.length; i++){
//
//                //file[i]의 이름이 null이 아니고 빈 문자열이 아니라면
//                if(file[i].getOriginalFilename() != null && !(file[i].getOriginalFilename().equals(""))){
//                    imageService.saveBannerImg(file[i],bannerDTO);
//                }
//
//            }
//        }




        Long bannerIdx = bannerService.register(bannerDTO);
        bannerDTO.setBannerIdx(bannerIdx);

        imageService.saveBannerImg(file,bannerDTO);


        redirectAttributes.addFlashAttribute("result", bannerIdx);
//        redirectAttributes.addAttribute("bannerIdx",bannerIdx);

        return "redirect:/banner/list";
    }


    @GetMapping("/banner/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("Banner listForm 도착 ");

        Page<BannerDTO> bannerDTOS = bannerService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(bannerDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", bannerDTOS);
        return "banner/list";
    }




    @GetMapping("/banner/modify/{bannerIdx}")
    public String modifyForm(@PathVariable Long bannerIdx, Model model) {

        log.info("Banner modifyForm 도착 " + bannerIdx);

        BannerDTO bannerDTO = bannerService.read(bannerIdx);

        log.info("수정 전 정보" + bannerDTO);
        model.addAttribute("bannerDTO", bannerDTO);
        return "banner/modify";
    }


    @PostMapping("/banner/modify")
    public String modifyProc(@Validated BannerDTO bannerDTO,
                             BindingResult bindingResult, Model model) {


        log.info("Banner modifyProc 도착 " + bannerDTO);



        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/banner/modify";
        }
        bannerService.modify(bannerDTO);

        log.info("업데이트 이후 정보 " + bannerDTO);

        return "redirect:/banner/list";
    }

    @GetMapping("/banner/delete/{bannerIdx}")
    public String deleteProc(@PathVariable Long bannerIdx) {
        bannerService.delete(bannerIdx);
        //서비스처리(삭제)
        return "redirect:/banner/list";
    }

    @GetMapping("/banner/{bannerIdx}")
    public String read(@PathVariable Long bannerIdx, Model model){

        BannerDTO bannerDTO = bannerService.read(bannerIdx);

        //이미지 목록 boardImgDTOList를 만든다.
        List<ImageDTO> ImgDTOList = imageService.imgList(bannerIdx);

        //boardDTO에 있는 dtoList 변수의 값을 boardImgDTOList로 셋 한다
        bannerDTO.setDtoList(ImgDTOList);


        model.addAttribute("bannerDTO",bannerDTO);

        return "banner/read";
    }
}
