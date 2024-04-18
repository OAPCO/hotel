package com.exam.hotelgers.Controller;



import com.exam.hotelgers.constant.RoleType;
import com.exam.hotelgers.dto.AdminDTO;
import com.exam.hotelgers.dto.BranchDTO;
import com.exam.hotelgers.dto.BrandDTO;
import com.exam.hotelgers.dto.DistDTO;
import com.exam.hotelgers.service.AdminService;
import com.exam.hotelgers.service.DistService;
import com.exam.hotelgers.service.SearchService;
import com.exam.hotelgers.util.PageConvert;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class AdminController {


    private final AdminService adminService;
    private final DistService distService;
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

        Page<DistDTO> distDTOS = distService.list(pageable);


        List<DistDTO> distList = searchService.distList();
        List<BranchDTO> branchList = searchService.branchList();
        List<BrandDTO> brandList = searchService.brandList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("branchList",branchList);
        model.addAttribute("brandList",brandList);
        model.addAttribute("list", distDTOS);


        return "admin/adminpage/storemembermange";
    }

    @PostMapping("/admin/adminpage/storemembermange")
    public String sdmProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                          @RequestParam(value="distName", required = false) String distName,
                          @RequestParam(value="distChiefEmail", required = false) String distChiefEmail,
                          @RequestParam(value="distChief", required = false) String distChief,
                          @RequestParam(value="distTel", required = false) String distTel
    ){


        log.info("들어온 첫 값 : @@ + " + distName);
        log.info("들어온 메일 : @@ + " + distChiefEmail);
        log.info("들어온 점주 : @@ + " + distChief);
        log.info("들어온 마지막 값 : @@ + " + distTel);



        Page<DistDTO> distDTOS = distService.searchmemadmin(distName,
                distChiefEmail,distChief,distTel,pageable);






        List<DistDTO> distList = searchService.distList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(distDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList",distList);
        model.addAttribute("list", distDTOS);

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
    public String sdmregisterget(){
        return "admin/adminpage/sdmregister";
    }
    @PostMapping("/admin/adminpage/sdmregister")
    public String sdmregisterpost(@Valid DistDTO distDTO,
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

        return "redirect:/admin/adminpage/storemembermange";}
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
    @GetMapping("/admin/pwchange")
    public String pwchangeForm(){

        return "admin/pwchange";
    }


}
