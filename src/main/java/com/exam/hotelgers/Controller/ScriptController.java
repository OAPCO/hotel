package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.BrandService;
import com.exam.hotelgers.service.ManagerService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.service.StoreService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ScriptController {

    private final StoreService storeService;
    private final BrandService brandService;
    private final ManagerService managerService;





    @GetMapping(value = "/selectstore", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectStore(SearchDTO searchDTO, Principal principal,Pageable pageable) throws Exception {

        log.info("ajax - selectStore Get 도착했음");

        Map<String, Object> result = new HashMap<>();

        List<BrandDTO> distOfBrand = brandService.distOfBrand(searchDTO);
        List<StoreDTO> distbrandOfStore = storeService.distbrandOfStore(searchDTO);


        result.put("distOfBrand", distOfBrand);
        result.put("distbrandOfStore", distbrandOfStore);

        return result;
    }


    @GetMapping(value = "/registerstore", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> registerStore(SearchDTO searchDTO) throws Exception {

        log.info("ajax - registerstore Get 도착했음");

        Map<String, Object> result = new HashMap<>();

        List<BrandDTO> distOfBrand = brandService.distOfBrand(searchDTO);
        List<ManagerDTO> distOfManager = managerService.distOfManager(searchDTO);

        result.put("distOfBrand", distOfBrand);
        result.put("distOfManager", distOfManager);

        return result;
    }


    @GetMapping("/logintype")
    public String Login(String roleType) {

        log.info("운영자들 login rest 겟매핑 들어옴");

        switch (roleType){
            case "ADMIN" : return "admin/login";

            case "DISTCHIEF" : return "admin/distchief/login";

            case "MANAGER" : return "admin/manager/login";
        }
        return "redirect:/admin/login";
    }



}