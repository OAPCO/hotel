package com.exam.hotelgers.Controller;



import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.constant.StoreGrade;
import com.exam.hotelgers.constant.StorePType;
import com.exam.hotelgers.constant.StoreStatus;
import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.AdminService;
import com.exam.hotelgers.service.DistService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.service.StoreService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;
    private final DistService distService;
    private final StoreService storeService;
    private final SearchService searchService;





    @GetMapping("/admin/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model) {

        log.info("admin listForm 도착");

        Page<AdminDTO> adminDTOList = adminService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(adminDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", adminDTOList);
        return "admin/list";
    }


    @GetMapping("/admin/register")
    public void registerForm() {

    }


    @PostMapping("/admin/register")
    public String registerProc(@Valid AdminDTO adminDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("admin registerProc 도착");


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(adminDTO);

        Long adminIdx = adminService.register(adminDTO);

        redirectAttributes.addFlashAttribute("result", adminIdx);

        return "redirect:/admin/list";
    }





    @GetMapping("/admin/delete/{adminIdx}")
    public String deleteProc(@PathVariable Long adminIdx) {


        adminService.delete(adminIdx);
        //서비스처리(삭제)
        return "redirect:/admin/list";
    }

    @GetMapping("/admin/adminpage/storemembermange")
    public String storemembermange(@PageableDefault(page = 1)Pageable pageable,Model model){

            log.info("storemangelist 도착");

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


        return "admin/adminpage/storemembermange";
    }

    @PostMapping("/admin/adminpage/storemembermange")
    public String sdmProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                          @RequestParam(value="distName", required = false) String distName,
                          @RequestParam(value="branchName", required = false) String branchName,
                          @RequestParam(value="storeName", required = false) String storeName,
                          @RequestParam(value="distChiefEmail", required = false) String distChiefEmail,
                          @RequestParam(value="distChief", required = false) String distChief,
                          @RequestParam(value="distTel", required = false) String distTel,
                          @RequestParam(value="storeStatus", required = false) StoreStatus storeStatus
                          ){


        log.info("들어온 별 값 : @@ + " + distName);
        log.info("들어온 상태 값 : @@ + " + storeStatus);
        log.info("들어온 피타입 값 : @@ + " + storeStatus);



        Page<StoreDTO> storeDTOS = storeService.searchListadminmem(distName,branchName,storeName,
                distChiefEmail,distChief,distTel,storeStatus,pageable);






        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<BrandDTO> brandList = searchService.brandList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", storeDTOS);

        return "admin/adminpage/storemembermange";
    }


    @GetMapping("/admin/adminpage/codemange")
    public String getProfile() {
       return "/admin/adminpage/codemange";
    }

@GetMapping("/admin/adminpage/storedistmange")
    public String storedistmange(@PageableDefault(page = 1)Pageable pageable,Model model){

        log.info("storemangelist 도착");

        Page<DistDTO> distDTOList = distService.list(pageable);

        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOList);

        model.addAllAttributes(pageinfo);
        model.addAttribute("list", distDTOList);
        return "admin/adminpage/storedistmange";
}

    @PostMapping("/admin/adminpage/storedistmange")
    public String sdmProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                          @RequestParam(value="distName", required = false) String distName,
                          @RequestParam(value="distChief", required = false) String distChief

    ){


        log.info("들어온 별 값 : @@ + " + distName);
        log.info("들어온 상태 값 : @@ + " + distChief);




        Page<DistDTO> distDTOS = distService.searchadminstoredistmange(distName,distChief,pageable);







        List<DistDTO> distList = searchService.distList();



        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("list", distDTOS);

        return "admin/adminpage/storedistmange";
    }

@GetMapping("/admin/adminpage/distregister")
    public String distregister(@PageableDefault(page = 1)Pageable pageable,Model model){
    log.info("storemangelist 도착");

    Page<DistDTO> distDTOList = distService.list(pageable);

    Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOList);

    model.addAllAttributes(pageinfo);
    model.addAttribute("list", distDTOList);
        return "admin/adminpage/distregister";
}
    @PostMapping("/admin/adminpage/distregister")
    public String registerProc(@Valid DistDTO distDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        log.info("dist registerProc 도착 " + distDTO);


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }

        log.info(distDTO);

        Long distIdx = distService.register(distDTO);

        redirectAttributes.addFlashAttribute("result", distIdx);

        return "redirect:/admin/adminpage/storedistmange";
    }
    @GetMapping("/admin/adminpage/sdmregister")
    public String sdmtregister(){
        return "admin/adminpage/sdmregister";
    }
    @GetMapping("/admin/adminpage/distChiefsearch")
    public String dcsearch(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distChief", required = false) String distChief

    ){


        log.info("들어온 상태 값 : @@ + " + distChief);




        Page<DistDTO> distDTOS = distService.searchadmindr(distChief,pageable);







        List<DistDTO> distList = searchService.distList();



        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("list", distDTOS);
        return "admin/adminpage/distChiefsearch";
    }
    @PostMapping("/admin/adminpage/distChiefsearch")
    public String dccsearch(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @RequestParam(value="distChief", required = false) String distChief

    ){


        log.info("들어온 상태 값 : @@ + " + distChief);




        Page<DistDTO> distDTOS = distService.searchadmindr(distChief,pageable);







        List<DistDTO> distList = searchService.distList();



        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("list", distDTOS);
        return "admin/adminpage/distChiefsearch";
    }
}


