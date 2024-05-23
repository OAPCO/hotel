package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.ReviewDTO;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.service.ReviewService;
import com.exam.hotelgers.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ReviewController {
    private final StoreService storeService;
    private final ReviewService reviewService;

    @GetMapping("/review/register/{storeIdx}")
    String registerForm(@PathVariable Long storeIdx, Model model) throws Exception {
        StoreDTO storeDTO = storeService.read(storeIdx);
        model.addAttribute("storeDTO",storeDTO);
        return "review/register";
    }

    @PostMapping("/review/register")
    public void registerProc(@ModelAttribute ReviewDTO reviewDTO) throws Exception {
        reviewService.register(reviewDTO);
    }
}
