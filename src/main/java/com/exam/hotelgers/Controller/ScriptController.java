package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.dto.SearchDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.service.BrandService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ScriptController {

    private final StoreService storeService;
    private final BrandService brandService;




    @GetMapping(value = "/selectstore", consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listForm(SearchDTO searchDTO) throws Exception {

        Map<String, Object> result = new HashMap<>();

        List<BrandDTO> distOfBrand = brandService.distOfBrand(searchDTO);
        List<StoreDTO> distbrandOfStore = storeService.distbrandOfStore(searchDTO);

        result.put("distOfBrand", distOfBrand);
        result.put("distbrandOfStore", distbrandOfStore);

        return result;
    }



}