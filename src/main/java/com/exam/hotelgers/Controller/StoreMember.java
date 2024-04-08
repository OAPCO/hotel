package com.exam.hotelgers.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreMember {
    @GetMapping("/storemember/list")
    public String Sto(){
        return "storemember/list";
    }

    @GetMapping("/storemember/register")
    public String st(){return "storemember/register";}

    @GetMapping("/storemanagement/list")
    public String stm(){return "storemanagement/list";}

    @GetMapping("/settlement/list")
    public String sts(){return "settlement/list";}

    @GetMapping("/detail/list")
    public String std(){return "detail/list";}
}
