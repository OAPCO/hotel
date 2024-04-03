package com.exam.hotelgers.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//클래스이름은 사용자 마음대로
//Component는 사용자가 만든 객체(오브젝트, 메소드)
//로그인을 성공했을 때 처리할 메소드
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession(); //클라이언트 정보 읽기
        if(session != null) { //클라이언트가 서버에 연결되어있으면
            String userid = authentication.getName(); //로그인한 아이디를 읽어온다.(프라이머리 식별자를 가져옴)
            session.setAttribute("userid", userid); //세션에 userid 라는 변수에 식별자가 저장되었다
        }
        super.setDefaultTargetUrl("/"); //simpleUrl....클래스에 저장
        super.onAuthenticationSuccess(request, response, authentication);

    }

}
