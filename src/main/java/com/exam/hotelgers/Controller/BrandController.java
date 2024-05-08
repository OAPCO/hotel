package com.exam.hotelgers.Controller;

import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.entity.*;
import com.exam.hotelgers.repository.DistRepository;
import com.exam.hotelgers.service.BrandService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BrandController {

    private final BrandService brandService;
    private final SearchService searchService;
    private final DistRepository distRepository;



    @GetMapping("/admin/distchief/brand/register")
    public String registerForm(Model model) {
        List<String> distCd = distRepository.findAllDistCds();
        model.addAttribute("distCd", distCd);
        return "/admin/distchief/brand/register";
    }


    @PostMapping("/admin/distchief/brand/register")
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

        return "redirect:/admin/distchief/brand/list";
    }



    @GetMapping("/admin/distchief/brand/list")
    public String selectForm(@PageableDefault(page=1) Pageable pageable, Principal principal, Model model) {
        log.info("brand listForm 도착 ");

        Page<BrandDTO> brandDTOS = brandService.list(pageable,principal);

        Map<String, Integer> pageinfo = PageConvert.Pagination(brandDTOS);
        model.addAllAttributes(pageinfo);

        model.addAttribute("list", brandDTOS);

        return "admin/distchief/brand/list";
    }




    @GetMapping("/admin/distchief/brand/modify/{brandIdx}")
    public String modifyForm(@PathVariable Long brandIdx, Model model) {

        log.info("brand modifyProc 도착 " + brandIdx);

        BrandDTO brandDTO = (BrandDTO) brandService.read(brandIdx);

        List<String> distCd = distRepository.findAllDistCds();
        model.addAttribute("distCd", distCd);

        log.info("수정 전 정보" + brandDTO);
        model.addAttribute("brandDTO", brandDTO);
        return "admin/distchief/brand/modify";
    }


    @PostMapping("/admin/distchief/brand/modify")
    public String modifyProc(@Validated BrandDTO brandDTO,
                             BindingResult bindingResult, Model model) {

        log.info("brand modifyProc 도착 " + brandDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "admin/distchief/brand/modify";
        }


        brandService.modify(brandDTO);

        log.info("업데이트 이후 정보 " + brandDTO);

        return "redirect:/admin/distchief/brand/list";
    }

    @GetMapping("/admin/distchief/brand/delete/{brandIdx}")
    public String deleteProc(@PathVariable Long brandIdx) {

        brandService.delete(brandIdx);

        return "redirect:/admin/distchief/brand/list";
    }

    @GetMapping("/admin/distchief/brand/{brandIdx}")
    public String readForm(@PathVariable Long brandIdx, Model model) {

        Map<String, Object> modelMap = brandService.read(brandIdx);
        log.info("modelMap: " + modelMap);

        Brand brand = (Brand) modelMap.get("brand");
        Manager manager = (Manager) modelMap.get("manager");
        Dist dist = (Dist) modelMap.get("dist");
        DistChief distChief = (DistChief) modelMap.get("distChief");
        List<Store> storeList = (List<Store>) modelMap.get("storeList");

        // 'BrandDTO'를 새로 생성하고 필요한 정보를 설정하세요.
        // 이 예제에서는 'Brand' 객체의 필드만 사용하고 있습니다.
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setBrandIdx(brand.getBrandIdx());
        brandDTO.setBrandCd(brand.getBrandCd());
        brandDTO.setBrandName(brand.getBrandName());

        // model에 추가
        model.addAttribute("brandDTO", brandDTO);
        model.addAttribute("manager", manager);
        model.addAttribute("dist", dist);
        model.addAttribute("distChief", distChief);
        model.addAttribute("storeList", storeList);

        return "admin/distchief/brand/read";
    }
}
