package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.BannerDTO;
import com.exam.hotelgers.dto.ImageDTO;
import com.exam.hotelgers.service.BannerService;
import com.exam.hotelgers.service.ImageService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
import java.io.IOException;
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
    private final HttpServletRequest request;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;



    @GetMapping("/banner/register")
    public String register() {
        return "banner/register";
    }


    @PostMapping("/banner/register")
    public String registerProc(@Valid BannerDTO bannerDTO,
                               BindingResult bindingResult,
                               @RequestParam("imgFile") List<MultipartFile> imgFile,
                               RedirectAttributes redirectAttributes) throws IOException {

        log.info("banner registerProc 도착" + bannerDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error 발생");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        Long bannerIdx = bannerService.register(bannerDTO,imgFile);


        redirectAttributes.addFlashAttribute("result", bannerIdx);

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
    public String modifyForm(@PathVariable Long bannerIdx, Model model)throws IOException {

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
    public String read(@PathVariable Long bannerIdx, Model model) throws IOException {

        //idx로 이미지 목록 조회
        List<ImageDTO> imageDTOList = imageService.getBannerImages(bannerIdx);

        BannerDTO bannerDTO = bannerService.read(bannerIdx);

        model.addAttribute("bannerDTO",bannerDTO);
        model.addAttribute("images",imageDTOList);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "banner/read";
    }
}
