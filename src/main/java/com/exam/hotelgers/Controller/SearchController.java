package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.service.AdminService;
import com.exam.hotelgers.service.BrandService;
import com.exam.hotelgers.util.PageConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SearchController {


    private final BrandService brandService;
    private final AdminService adminService;


    @GetMapping("/window/branchchiefsearch")
    public String branchSearch(@PageableDefault(page=1) Pageable pageable, Model model) {

        Page<AdminDTO> branchChiefDTOS = adminService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(branchChiefDTOS);
        model.addAllAttributes(pageinfo);

        model.addAttribute("list", branchChiefDTOS);

        return "window/branchchiefsearch";
    }


    @GetMapping("/window/brandsearch")
    public String brandSearch(@PageableDefault(page=1) Pageable pageable, Model model) {

        Page<BrandDTO> brandDTOS = brandService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(brandDTOS);
        model.addAllAttributes(pageinfo);

        model.addAttribute("list", brandDTOS);

        return "window/brandsearch";
    }

}
