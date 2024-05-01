package com.exam.hotelgers.Controller;



import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.AdminService;
import com.exam.hotelgers.service.DistService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.service.StoreService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class TestController {


    private final AdminService adminService;
    private final DistService distService;
    private final SearchService searchService;
    private final StoreService storeService;


    @GetMapping("/test")
    public void registerForm() {

    }


    @GetMapping("/test/register")
    public String register123(Model model) throws Exception{

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();
        List<ManagerDTO> managerList = searchService.managerList();

        model.addAttribute("distList", distList);
        model.addAttribute("brandList", brandList);
        model.addAttribute("managerList", managerList);


        return "test/register";
    }


    @PostMapping("/test/register")
    public String registerProc123(@Valid StoreDTO storeDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes,
                                  MultipartFile imgFile) throws Exception{

        log.info("store registerProc 도착 " + storeDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long storeIdx = storeService.register(storeDTO, imgFile);


        redirectAttributes.addFlashAttribute("result", storeIdx);

        return "redirect:/test/register";
    }




}
