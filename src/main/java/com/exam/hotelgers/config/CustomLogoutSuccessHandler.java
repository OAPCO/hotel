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
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        //로그아웃시 섹션을 제거한다.
        if(session != null) {
            session.invalidate();
        }

        super.setDefaultTargetUrl("/");
        super.onLogoutSuccess(request, response, authentication);
    }

}
