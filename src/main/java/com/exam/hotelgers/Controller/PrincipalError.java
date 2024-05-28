package com.exam.hotelgers.Controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import java.security.Principal;

@ControllerAdvice
public class PrincipalError {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, WebRequest request, Principal principal) {
        if (principal == null) {
            // 사용자가 로그인되어 있지 않은 경우
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/member/login");
            return modelAndView;
        } else {
            // 로그인은 되어 있지만 다른 예외가 발생한 경우
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("error/errorpage"); // 에러 페이지로 이동하거나 원하는 페이지로 설정
            modelAndView.addObject("errorMessage", ex.getMessage()); // 에러 메시지 전달
            return modelAndView;
        }
    }
}
