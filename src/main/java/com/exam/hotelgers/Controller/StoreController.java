package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class StoreController {

    private final StoreService storeService;
    private final DistService distService;
    private final SearchService searchService;
    private final BrandService brandService;
    private final DistChiefService distChiefService;
    private final ManagerService managerService;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;


    @GetMapping("/admin/distchief/store/register")
    public String register(Model model,Principal principal) throws Exception {

        List<DistDTO> distDTOS = distService.distSearchforUserId(principal);
        List<BrandDTO> brandDTOS = brandService.brandSearchforUserId(principal);
        List<ManagerDTO> managerDTOS = managerService.managerSearch(principal.getName());



        model.addAttribute("distList", distDTOS);
        model.addAttribute("brandList", brandDTOS);
        model.addAttribute("managerList", managerDTOS);


        return "admin/distchief/store/register";
    }


    @PostMapping("/admin/distchief/store/register")
    public String registerProc(@Valid StoreDTO storeDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               SearchDTO searchDTO,
                               MultipartFile imgFile) throws Exception {

        log.info("store registerProc 도착 " + storeDTO);

        log.info("아이디확인@@@@@@ " + searchDTO.getManagerId());


        if (bindingResult.hasErrors()) {
            log.info("has error@@@@@@@@@");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
        }


        Long storeIdx = storeService.register(storeDTO, searchDTO, imgFile);


        redirectAttributes.addFlashAttribute("result", storeIdx);

        return "redirect:/admin/distchief/store/list";
    }


    @PostMapping("/admin/distchief/store/list")
    public String listProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                           @Valid SearchDTO searchDTO, Principal principal
    ) throws Exception {


        Page<StoreDTO> storeDTOS = storeService.searchList(searchDTO, pageable, principal);

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList", distList);
        model.addAttribute("brandList", brandList);
        model.addAttribute("list", storeDTOS);
        return "admin/distchief/store/list";
    }


    @GetMapping("/admin/distchief/store/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable, Model model, Principal principal
    ) throws Exception {

        log.info("store listForm 도착 ");

        List<DistDTO> distDTOS = distChiefService.distChiefOfDistList(principal);
        Page<StoreDTO> storeDTOS = storeService.list(pageable, principal);


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList", distDTOS);
        model.addAttribute("list", storeDTOS);
        //S3 이미지정보전달
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/distchief/store/list";
    }


    @GetMapping("/admin/distchief/store/modify/{storeIdx}")
    public String modifyForm(@PathVariable Long storeIdx, Model model) throws Exception {

        log.info("store modifyProc 도착 " + storeIdx);

        StoreDTO storeDTO = storeService.read(storeIdx);

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();

        model.addAttribute("distList", distList);
        model.addAttribute("brandList", brandList);

        log.info("수정 전 정보" + storeDTO);
        model.addAttribute("storeDTO", storeDTO);
        return "admin/distchief/store/modify";
    }


    @PostMapping("/admin/distchief/store/modify")
    public String modifyProc(@Validated StoreDTO storeDTO,
                             MultipartFile imgFile,
                             BindingResult bindingResult, Model model) throws Exception {

        log.info("store modifyProc 도착 " + storeDTO);

        if (bindingResult.hasErrors()) {

            log.info("업데이트 에러 발생");

            return "admin/distchief/store/modify";
        }

        storeService.modify(storeDTO, imgFile);

        log.info("업데이트 이후 정보 " + storeDTO);

        return "redirect:/admin/distchief/store/list";
    }

    @GetMapping("/admin/distchief/store/delete/{storeIdx}")
    public String deleteProc(@PathVariable Long storeIdx) throws Exception {

        storeService.delete(storeIdx);

        return "redirect:/admin/distchief/store/list";
    }


    @GetMapping("/admin/distchief/store/{storeIdx}")
    public String readForm(@PathVariable Long storeIdx, Model model) throws Exception {
        StoreDTO storeDTO = storeService.read(storeIdx);

        if (storeDTO == null) {
            model.addAttribute("processMessage", "존재하지 않는 자료입니다.");
            return "redirect:/admin/distchief/store/list";
        }

        model.addAttribute("store", storeDTO);
        model.addAttribute("brand", storeDTO.getBrandDTO());
        model.addAttribute("dist", storeDTO.getDistDTO());
        model.addAttribute("distChief", storeDTO.getDistDTO().getDistChiefDTO());
        model.addAttribute("manager", storeDTO.getManagerDTO());
        model.addAttribute("roomList", storeDTO.getRoomDTOList());
        model.addAttribute("menuCateList", storeDTO.getMenuCateDTOList());

        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        log.info("Detail Menu List: " + storeDTO.getDetailmenuDTOList());
        log.info("Menu Category List: " + storeDTO.getMenuCateDTOList());

        return "admin/distchief/store/read";
    }


    @PostMapping("/admin/admin/manage/storelist")
    public String adminlistProc(@PageableDefault(page = 1) Pageable pageable, Model model,
                                @Valid SearchDTO searchDTO
    ) throws Exception {

        Page<StoreDTO> storeDTOS = storeService.adminSearchList(searchDTO, pageable);

        List<DistDTO> distList = searchService.distList();
        List<BrandDTO> brandList = searchService.brandList();


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList", distList);
        model.addAttribute("brandList", brandList);
        model.addAttribute("list", storeDTOS);
        return "admin/admin/manage/storelist";
    }


    @GetMapping("/admin/admin/manage/storelist")
    public String adminlistForm(@PageableDefault(page = 1) Pageable pageable, Model model
    ) throws Exception {

        log.info("store listForm 도착 ");

        Page<DistDTO> distDTOS = distService.list(pageable);
        Page<StoreDTO> storeDTOS = storeService.listAll(pageable);


        Map<String, Integer> pageinfo = PageConvert.Pagination(storeDTOS);

        model.addAllAttributes(pageinfo);
        model.addAttribute("distList", distDTOS);
        model.addAttribute("list", storeDTOS);
        //S3 이미지정보전달
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/admin/manage/storelist";
    }

    @GetMapping("/member/memberpage/test/{storeIdx}")
    public String testpage(@PathVariable Long storeIdx, Model model) throws Exception {
        StoreDTO storeDTO = storeService.read(storeIdx);
        model.addAttribute("storeDTO", storeDTO);
        return "member/memberpage/test";
    }





    @GetMapping("/admin/manager/storeinfo")
    public String deleteProc(Principal principal,Model model) throws IOException {

        StoreDTO storeDTO = managerService.managerOfStore(principal);

        model.addAttribute("storeDTO", storeDTO);
        model.addAttribute("bucket", bucket);
        model.addAttribute("region", region);
        model.addAttribute("folder", folder);

        return "admin/manager/storeinfo";
    }


}