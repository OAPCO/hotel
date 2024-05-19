package com.exam.hotelgers.Controller;


import com.exam.hotelgers.dto.MemberDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ReactTestController {


    MemberService memberService;

    @PostMapping(value="/testData")
    public Map<Integer, String> testData(@RequestBody List<String> params){
        Map<Integer, String> data = new HashMap<>();
        data.put(1,"사과");
        data.put(2,"포도");
        data.put(3,"바나나");

        int i = 4;
        for(String param : params){
            data.put(i, param);
            i++;
        }

        return data;
    }



}
