package com.exam.hotelgers.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreMember {
    @GetMapping("/storemember/list")
    public String Sto(){
        return "storemember/list";
    }
}
