package com.exam.hotelgers.Controller;

import com.exam.hotelgers.dto.ReviewDTO;
import com.exam.hotelgers.dto.RoomDTO;
import com.exam.hotelgers.dto.StoreDTO;
import com.exam.hotelgers.entity.Member;
import com.exam.hotelgers.repository.MemberRepository;
import com.exam.hotelgers.service.ReviewService;
import com.exam.hotelgers.service.StoreService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ReviewController {
    private final StoreService storeService;
    private final ReviewService reviewService;
    private final MemberRepository memberRepository;

    @GetMapping("/review/register/{storeIdx}")
    String registerForm(@PathVariable Long storeIdx, Model model) throws Exception {
        StoreDTO storeDTO = storeService.read(storeIdx);
        model.addAttribute("storeDTO",storeDTO);
        return "review/register";
    }

    @PostMapping("/review/register")
    public void registerProc(@ModelAttribute ReviewDTO reviewDTO, HttpServletResponse response, Principal principal) throws Exception {
        String userId = principal.getName();
        log.info("회원아이디"+userId);
        Member member = memberRepository.findByMemberEmail(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        // ReviewDTO에 회원의 식별자 설정
        reviewDTO.setMemberIdx(member.getMemberIdx());

        log.info("ReviewDTO: " + reviewDTO); // 로그 추가
        reviewService.register(reviewDTO);

        // 응답으로 JavaScript를 이용한 alert 창을 보냅니다.
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('리뷰가 성공적으로 등록되었습니다.');window.close();</script>");
    }



}
