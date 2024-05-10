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




    @GetMapping("/admin/admin/banner/register")
    public String bannerRegister() {
        return "admin/admin/banner/register";
    }


    @PostMapping("/admin/admin/banner/register")
    public String bannerRegisterProc(@Valid BannerDTO bannerDTO,
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

        return "redirect:/admin/admin/banner/list";
    }


    @GetMapping("/admin/admin/banner/list")
    public String bannerListForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        Page<BannerDTO> bannerDTOS = bannerService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(bannerDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", bannerDTOS);
        return "admin/admin/banner/list";
    }



    @GetMapping("/admin/admin/banner/delete/{bannerIdx}")
    public String deleteProc(@PathVariable Long bannerIdx) {


        bannerService.delete(bannerIdx);
        return "redirect:/admin/admin/banner/list";
    }

    @GetMapping("/admin/admin/banner/{bannerIdx}")
    public String read(@PathVariable Long bannerIdx, Model model) throws IOException {

        List<ImageDTO> imageDTOList = imageService.getBannerImages(bannerIdx);

        BannerDTO bannerDTO = bannerService.read(bannerIdx);

        model.addAttribute("bannerDTO",bannerDTO);
        model.addAttribute("images",imageDTOList);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/admin/banner/read";
    }




    @PostMapping("/banner/modify")
    public String modifyProc(@RequestParam Long bannerIdx,
                             @RequestParam("imgFile") List<MultipartFile> imgFile,
                             Model model) throws IOException {


        imageService.bannerImageregister(imgFile,bannerIdx);

        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        String referer = request.getHeader("referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        } else {
            return "redirect:/admin/admin/banner/list";
        }
    }


}
