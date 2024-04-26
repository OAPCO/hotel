package com.exam.hotelgers.Controller;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StoreController {

    private final StoreService storeService;
    private final SearchService searchService;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;



    @GetMapping("/admin/distchief/store/register")
    public String register(Model model) throws Exception{

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();

        model.addAttribute("distList", distList);
        model.addAttribute("brandList", brandList);


        return "admin/distchief/store/register";
    }


    @PostMapping("/admin/distchief/store/register")
    public String registerProc(@Valid StoreDTO storeDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               MultipartFile imgFile) throws Exception{

        log.info("store registerProc 도착 " + storeDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long storeIdx = storeService.register(storeDTO, imgFile);

//        storeDTO.setStoreIdx(storeIdx);

        redirectAttributes.addFlashAttribute("result", storeIdx);

        return "redirect:/admin/distchief/store/list";
    }





    @PostMapping("/admin/distchief/store/list")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @Valid SearchDTO searchDTO
    ) throws Exception{




        Page<StoreDTO> storeDTOS = storeService.searchList(searchDTO, pageable);



        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", storeDTOS);
        return "admin/distchief/store/list";
    }




    @GetMapping("/admin/distchief/store/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model

    ) throws Exception {

        log.info("store listForm 도착 ");

        Page<StoreDTO> storeDTOS = storeService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", storeDTOS);
        //S3 이미지정보전달
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/distchief/store/list";
    }





    @GetMapping("/admin/distchief/store/modify/{storeIdx}")
    public String modifyForm(@PathVariable Long storeIdx, Model model) throws Exception {

        log.info("store modifyProc 도착 " + storeIdx);

        StoreDTO storeDTO = storeService.read(storeIdx);

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();

        model.addAttribute("distList", distList);
        model.addAttribute("brandList", brandList);

        log.info("수정 전 정보" + storeDTO);
        model.addAttribute("storeDTO", storeDTO);
        return "admin/distchief/store/modify";
    }


    @PostMapping("/admin/distchief/store/modify")
    public String modifyProc(@Validated StoreDTO storeDTO,
                             MultipartFile imgFile,
                             BindingResult bindingResult, Model model) throws Exception{

        log.info("store modifyProc 도착 " + storeDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "admin/distchief/store/modify";
        }

        storeService.modify(storeDTO, imgFile);

        log.info("업데이트 이후 정보 " + storeDTO);

        return "redirect:/admin/distchief/store/list";
    }

    @GetMapping("/admin/distchief/store/delete/{storeIdx}")
    public String deleteProc(@PathVariable Long storeIdx) throws Exception{

        storeService.delete(storeIdx);

        return "redirect:/admin/distchief/store/list";
    }


    @GetMapping("/admin/distchief/store/{storeIdx}")
    public String readForm(@PathVariable Long storeIdx, Model model) throws Exception{


        StoreDTO storeDTO = storeService.read(storeIdx);
        model.addAttribute("storeDTO", storeDTO);


        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        if(storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/admin/distchief/store/list";
        }

        log.info("디테일메뉴 리스트: " + storeDTO.getDetailmenuDTOList());
        log.info("메뉴카테고리 리스트: " + storeDTO.getMenuCateDTOList());

        return "admin/distchief/store/read";
    }











    //테스트용
    @GetMapping("/admin/distchief/store/imagetest")
    public String test(@PageableDefault(page = 1) Pageable pageable, Model model
    ) throws Exception {

        Page<StoreDTO> storeDTOS = storeService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();




        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", storeDTOS);
        //S3 이미지정보전달
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/distchief/store/imagetest";
    }





}