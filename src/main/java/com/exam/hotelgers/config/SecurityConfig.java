package com.exam.hotelgers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//사용자 커스텀
//권한부여
//로그인/로그아웃 설정
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //Bean 사용자가 설정
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //암호를 암호화
    }
    //페이지나 맵핑에 권한를 부여
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        //맵핑에 권한
        http.authorizeHttpRequests((auth)-> {

            //제약없음
            auth.requestMatchers("/**", "/assets/**").permitAll();

            //로그인이 안된 사용자들에 대한 제약
            auth.requestMatchers("/member/login", "/admin/login", "/member/register","/login").permitAll();

            //일반회원이 사용한 제약
            auth.requestMatchers("/member/update","/member/read","/member/logout","/logout").hasAnyRole("USER", "MANAGER", "ADMIN");
            //관리자 사용한 제약
            auth.requestMatchers("/member/**","/admin/logout","/logout").hasAnyRole("MANAGER", "ADMIN");
            //최종 운영자에 대한 제약
            auth.requestMatchers("/admin/**").hasAnyRole("ADMIN");
        });



        //로그인 커스텀
        http.formLogin(login->login
                .loginPage("/member/login")  //member/login, admin/login 사용자 로그인페이지(맵핑)
                .defaultSuccessUrl("/",true) //로그인성공시 이동할 맵핑
                .failureUrl("/member/login?error") //로그인 실패시 이동할 맵핑
                .usernameParameter("memberEmail") //로그인서비스에서 사용자아이디로 쓰이는 변수
                .successHandler(new CustomLoginSuccessHandler()) //성공시 추가처리할 메소드
                .permitAll() //모든 이용자가 접근 가능
        );


//        http.formLogin(login->login
//                .loginPage("/admin/login")  //member/login, admin/login 사용자 로그인페이지(맵핑)
//                .defaultSuccessUrl("/",true) //로그인성공시 이동할 맵핑
//                .failureUrl("/member/login?error") //로그인 실패시 이동할 맵핑
//                .usernameParameter("memberEmail") //로그인서비스에서 사용자아이디로 쓰이는 변수
//                .successHandler(new CustomLoginSuccessHandler()) //성공시 추가처리할 메소드
//                .permitAll() //모든 이용자가 접근 가능
//        );



        http.csrf(AbstractHttpConfigurer::disable);
        //필수@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        //로그아웃 커스텀
        http.logout(logout->logout
                .logoutUrl("/logout") //로그아웃을 처리할 맵핑명
                .logoutSuccessUrl("/") //로그아웃 후 이동할 맵핑
        );

        return http.build();
    }
}
