package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.ManagerDTO;
import com.exam.hotelgers.service.BrandService;
import com.exam.hotelgers.service.ManagerService;
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


    private final ManagerService managerService;
    private final BrandService brandService;


    @GetMapping("/window/managersearch")
    public String managerSearch(@PageableDefault(page=1) Pageable pageable, Model model) {

        Page<ManagerDTO> managerDTOS = managerService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(managerDTOS);
        model.addAllAttributes(pageinfo);

        model.addAttribute("list", managerDTOS);

        return "window/managersearch";
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
