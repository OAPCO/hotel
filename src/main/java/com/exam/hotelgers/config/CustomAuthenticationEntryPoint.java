package com.exam.hotelgers.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

//인증처리 클래스
//사용자 맞게 변경->인터페이스->@Override
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //request->req, response->resp
    //try~catch 오류발생하면 클래스내에서 오류를 무시->외부클래스 오류정보X
    //throws 오류발생하면 오류를 무시하고 오류정보를 호출한 곳에 반환
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증실패");
    }
}
