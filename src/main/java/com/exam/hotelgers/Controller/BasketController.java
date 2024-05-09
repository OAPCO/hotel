package com.exam.hotelgers.Controller;


import com.exam.hotelgers.dto.BasketDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.service.*;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class BasketController {



    private final DistService distService;
    private final BasketService basketService;
    private final SearchService searchService;
    private final DistChiefService distChiefService;




    @GetMapping("/member/memberpage/basket/{memberIdx}")
    public String listForm(Long memberIdx,@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("basket listForm 도착");

        Page<BasketDTO> basketDTOList = basketService.list(memberIdx,pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(basketDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", basketDTOList);
        return "member/memberpage/basket/memberIdx";
    }



}
