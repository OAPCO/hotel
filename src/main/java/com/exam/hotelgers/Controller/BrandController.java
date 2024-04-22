package com.exam.hotelgers.Controller;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.Brand;
import com.exam.hotelgers.entity.Dist;
import com.exam.hotelgers.repository.DistRepository;
import com.exam.hotelgers.service.BrandService;
import com.exam.hotelgers.service.DistService;
import com.exam.hotelgers.service.PageService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BrandController {

    private final BrandService brandService;
    private final SearchService searchService;
    private final DistRepository distRepository;



    @GetMapping("/brand/register")
    public String registerForm(Model model) {
        List<String> distCd = distRepository.findAllDistCds();
        model.addAttribute("distCd", distCd);
        return "/brand/register";
    }


    @PostMapping("/brand/register")
    public String registerProc(@Valid BrandDTO brandDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("brand registerProc 도착 " + brandDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(brandDTO);

        Long brandIdx = brandService.register(brandDTO);

        redirectAttributes.addFlashAttribute("result", brandIdx);

        return "redirect:/brand/list";
    }


    //전체 목록
    @GetMapping("/brand/list")
    public String selectForm(@PageableDefault(page=1) Pageable pageable, Model model) {
        log.info("brand listForm 도착 ");

        Page<BrandDTO> brandDTOS = brandService.list(pageable);

        List<DistDTO> distList = searchService.distList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(brandDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("brandList", brandDTOS);
        return "brand/list";
    }






    @PostMapping("/brand/list")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distChiefEmail", required = false) String distChiefEmail,
                           @RequestParam(value="distName", required = false) String distName,
                           @RequestParam(value="brandName", required = false) String brandName,
                           @RequestParam(value="brandCd", required = false) String brandCd,
                           @RequestParam(value="StoreStatus", required = false) StoreStatus storestatus
    )
    {


        Page<BrandDTO> brandDTOS = brandService.searchList(distChiefEmail,distName, brandName, brandCd, storestatus,
                pageable);


        List<DistDTO> distList = searchService.distList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(brandDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("storeStatuses", storestatus);
        model.addAttribute("distList",distList);
        model.addAttribute("brandList", brandDTOS);

        return "brand/list";
    }




    @GetMapping("/brand/modify/{brandIdx}")
    public String modifyForm(@PathVariable Long brandIdx, Model model) {

        log.info("brand modifyProc 도착 " + brandIdx);

        BrandDTO brandDTO = brandService.read(brandIdx);

        List<String> distCd = distRepository.findAllDistCds();
        model.addAttribute("distCd", distCd);

        log.info("수정 전 정보" + brandDTO);
        model.addAttribute("brandDTO", brandDTO);
        return "brand/modify";
    }


    @PostMapping("/brand/modify")
    public String modifyProc(@Validated BrandDTO brandDTO,
                             BindingResult bindingResult, Model model) {

        log.info("brand modifyProc 도착 " + brandDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "/brand/modify";
        }


        brandService.modify(brandDTO);

        log.info("업데이트 이후 정보 " + brandDTO);

        return "redirect:/brand/list";
    }

    @GetMapping("/brand/delete/{brandIdx}")
    public String deleteProc(@PathVariable Long brandIdx) {

        brandService.delete(brandIdx);

        return "redirect:/brand/list";
    }

    @GetMapping("/brand/{brandIdx}")
    public String readForm(@PathVariable Long brandIdx, Model model) {
        BrandDTO brandDTO=brandService.read(brandIdx);
        //서비스에서 값을 받으면 반드시 model로 전달
        model.addAttribute("brandDTO",brandDTO);
        return "brand/read";
    }
}
