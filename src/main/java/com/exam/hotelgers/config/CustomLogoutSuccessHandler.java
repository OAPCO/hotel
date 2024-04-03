package com.exam.hotelgers.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//클래스이름은 사용자 마음대로
//Component는 사용자가 만든 객체(오브젝트, 메소드)
//로그인을 성공했을 때 처리할 메소드
@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession(); //클라이언트 정보 읽기
        if(session != null) { //클라이언트가 서버에 연결되어있으면
            session.invalidate(); //섹션을 삭제
        }
        super.setDefaultTargetUrl("/login"); //simpleUrl....클래스에 저장
        super.onLogoutSuccess(request, response, authentication);

    }

}
