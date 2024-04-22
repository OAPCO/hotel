package com.exam.hotelgers.Controller;

import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StoreController {
    
    private final StoreService storeService;
    private final ImageService imageService;
    private final SearchService searchService;

    @Value("C:/uploads/")
    private String uploadPath;

    public String makeDir(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String now = sdf.format(date);

        String path = uploadPath + "\\\\" + now;

        File file = new File(path);
        if(file.exists() == false){
            file.mkdir();
        }

        return path;
    }



    @GetMapping("/distchief/store/register")
    public String register(Model model) {
        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();

        model.addAttribute("distList", distList);
        model.addAttribute("brandList", brandList);

        return "distchief/store/register";
    }

    @PostMapping("/distchief/store/register")
    public String registerProc(@Valid StoreDTO storeDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @RequestParam(name = "file", required = false) MultipartFile file) {

        log.info("store registerProc 도착 " + storeDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }



        Long storeIdx = storeService.register(storeDTO);
        storeDTO.setStoreIdx(storeIdx);

        imageService.saveStoreImg(file,storeDTO);


        log.info("열거형확인@@@@@type는 " + storeDTO.getStorePType());
        log.info("열거형확인@@@@@status는 " + storeDTO.getStoreStatus());


        redirectAttributes.addFlashAttribute("result", storeIdx);

        return "redirect:/distchief/store/list";
    }


    @PostMapping("/distchief/store/list")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distName", required = false) String distName,
                           @RequestParam(value="branchName", required = false) String branchName,
                           @RequestParam(value="storeName", required = false) String storeName,
                           @RequestParam(value="storeGrade", required = false) StoreGrade storeGrade,
                           @RequestParam(value="storeCd", required = false) String storeCd,
                           @RequestParam(value="storeChiefEmail", required = false) String storeChiefEmail,
                           @RequestParam(value="storeChief", required = false) String storeChief,
                           @RequestParam(value="brandName", required = false) String brandName,
                           @RequestParam(value="storeStatus", required = false) StoreStatus storeStatus,
                           @RequestParam(value="storePType", required = false) StorePType storePType
                           ){


        log.info("들어온 총판 @@@@@ + " + distName);
        log.info("들어온 별 값 : @@ + " + storeGrade);
        log.info("들어온 상태 값 : @@ + " + storeStatus);
        log.info("들어온 피타입 값 : @@ + " + storePType);



        Page<StoreDTO> storeDTOS = storeService.searchList(distName,branchName,storeName,storeGrade,
                storeCd,storeChiefEmail,storeChief,brandName,storeStatus,storePType, pageable);






        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<BrandDTO> brandList = searchService.brandList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", storeDTOS);
        return "distchief/store/list";
    }

    @GetMapping("/distchief/store/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model
                           ) {

        log.info("store listForm 도착 ");

        Page<StoreDTO> storeDTOS = storeService.list(pageable);

        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<BrandDTO> brandList = searchService.brandList();




        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", storeDTOS);
        return "distchief/store/list";
    }





    @GetMapping("/distchief/store/modify/{storeIdx}")
    public String modifyForm(@PathVariable Long storeIdx, Model model) {

        log.info("store modifyProc 도착 " + storeIdx);

        StoreDTO storeDTO = storeService.read(storeIdx);

        log.info("수정 전 정보" + storeDTO);
        model.addAttribute("storeDTO", storeDTO);
        return "distchief/store/modify";
    }


    @PostMapping("/distchief/store/modify")
    public String modifyProc(@Validated StoreDTO storeDTO,
                             BindingResult bindingResult, Model model) {

        log.info("store modifyProc 도착 " + storeDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "distchief/store/modify";
        }


        storeService.modify(storeDTO);

        log.info("업데이트 이후 정보 " + storeDTO);

        return "redirect:/distchief/store/list";
    }

    @GetMapping("/distchief/store/delete/{storeIdx}")
    public String deleteProc(@PathVariable Long storeIdx) {

        storeService.delete(storeIdx);

        return "redirect:/distchief/store/list";
    }


    @GetMapping("/distchief/store/{storeIdx}")
    public String readForm(@PathVariable Long storeIdx, Model model) {


        StoreDTO storeDTO = storeService.read(storeIdx);
        model.addAttribute("storeDTO", storeDTO);

        log.info("가져온 룸목록은@@@@@@@@@ : " +  storeDTO.getRoomDTOList());
        log.info("가져온 메뉴카테목록은@@@@@@@@@ : " +  storeDTO.getMenuCateDTOList());
        log.info("가져온 디테일메뉴목록은@@@@@@@@@ : " +  storeDTO.getDetailmenuDTOList());


        if(storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:distchief/store/list";
        }

        return "distchief/store/read";
    }


}
