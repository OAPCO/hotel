package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.*;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.service.*;
import com.exam.hotelgers.util.PageConvert;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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
    private final MemberService memberService;
    private final ImageService imageService;


    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    @Value("${cloud.aws.region.static}")
    public String region;
    @Value("${imgUploadLocation}")
    public String folder;





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

    @GetMapping("/emailcheck/{memberEmail}") //여기에서 중복확인
    public ResponseEntity<Integer> checkEmailDuplication(@PathVariable String memberEmail) {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setMemberEmail(memberEmail);
        int result = memberService.checkEmailDuplication(searchDTO);
        log.info("중복확인"+result+memberEmail);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/api/kakao/userInfo")
    public String receiveKakaoUserInfo(@RequestBody String userInfo, MemberDTO memberDTO, Model model) throws Exception {
        String loginInfo = memberService.kakaoregister(userInfo, memberDTO);



        // Extract the email and password from the returned loginInfo
        String[] loginDetails = loginInfo.split(":");
        String memberEmail = loginDetails[0];
        String memberPassword = loginDetails[1];
        log.info(memberEmail + memberPassword);


//        log.info("로그인 시도중 : 아이디 - " + memberEmail);
//        log.info("로그인 시도중 : 비밀번호 - " + memberPassword);
//
//        model.addAttribute("loginInfo",loginInfo);



//        // Create a map to hold the login details
//        Map<String, String> loginPayload = new HashMap<>();
//        loginPayload.put("userid", memberEmail);
//        loginPayload.put("password", memberPassword);
//
//        // Send a POST request to the member/login endpoint
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(loginPayload, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        log.info("카카오 로그인 전송 "+requestEntity);
//        ResponseEntity<String> response = restTemplate.postForEntity("/member/login", requestEntity, String.class);


            return loginInfo;
    }





    //객실 사진 상세보기 페이지
    @GetMapping(value = "/roomimage/{roomIdx}",consumes = MediaType.ALL_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ImageDTO> roomreadform(Model model, @PathVariable Long roomIdx) throws Exception {


        List<ImageDTO> imageDTOS = imageService.roomImageSearch(roomIdx);

        return imageDTOS;
    }




}


