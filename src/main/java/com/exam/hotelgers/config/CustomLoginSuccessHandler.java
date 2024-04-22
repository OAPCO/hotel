package com.exam.hotelgers.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();

        if(session != null) {
            String userid = authentication.getName();
            session.setAttribute("userid", userid);

            //사용자가 로그인을 성공하였을 때 이동할 페이지
            boolean isUser = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));
            if(isUser) {
                super.setDefaultTargetUrl("/member/list");
            }

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            if(isAdmin) {
                super.setDefaultTargetUrl("/admin/adminpage/distregister");
            }

            boolean isManager = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MANAGER"));
            if(isManager) {
                super.setDefaultTargetUrl("/manager/list");
            }


            boolean isDistChief = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_DISTCHIEF"));
            if(isDistChief) {
                super.setDefaultTargetUrl("/distchief/store/list");
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}

