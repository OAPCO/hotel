package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.BranchChiefDTO;
import com.exam.hotelgers.dto.BranchDTO;
import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.service.BranchChiefService;
import com.exam.hotelgers.service.BranchService;
import com.exam.hotelgers.service.BrandService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SearchController {

    private final BranchChiefService branchChiefService;
    private final BrandService brandService;


    @GetMapping("/window/branchchiefsearch")
    public String branchSearch(@PageableDefault(page=1) Pageable pageable, Model model) {

        Page<BranchChiefDTO> branchChiefDTOS = branchChiefService.list(pageable);

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
