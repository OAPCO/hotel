package com.exam.hotelgers.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StartController {
    @GetMapping("/brandselectlist")
    public String brandselectlist(){
        return "brandselectlist";
    }
}
