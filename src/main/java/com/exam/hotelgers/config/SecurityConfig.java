package com.exam.hotelgers.config;

import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.service.AdminLoginService;
import com.exam.hotelgers.service.LoginService;
import com.exam.hotelgers.service.ManagerLoginService;
import com.exam.hotelgers.service.MemberLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //암호
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //관리자 로그인 서비스
    @Bean
    public AdminLoginService adminLoginService() {
        return new AdminLoginService();
    }
    //일반 로그인 서비스
    @Bean
    public MemberLoginService memberLoginService() {
        return new MemberLoginService();
    }
    //관리자 로그인처리 등록
    @Bean
    public ManagerLoginService managerLoginService() {
        return new ManagerLoginService();
    }


    @Bean
    public DaoAuthenticationProvider adminProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminLoginService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    //일반 로그인처리 등록
    @Bean
    public DaoAuthenticationProvider memberProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(memberLoginService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider managerProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(managerLoginService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        //사용권한
        http.securityMatcher("/admin/**").authorizeRequests()
                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/login", "/logout", "/member/register", "/admin/register").permitAll()
                .requestMatchers("/member/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/admin/**", "/member/**").hasRole("ADMIN");

        //관리자회원 로그인
        http.formLogin(login -> login
                .defaultSuccessUrl("/admin/list", true)
                .failureUrl("/admin/login?error=true")
                .loginPage("/admin/login")
                .usernameParameter("adminid") //entity에 아이디 필드명
                .permitAll()
                .successHandler(new CustomLoginSuccessHandler()));

        //CSRF 보호를 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        //로그아웃
        http.logout(logout-> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/admin/login")); //로그아웃

        //관리자인 경우 관리자 로그인처리
        http.authenticationProvider(adminProvider());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        //사용권한
        http.authorizeRequests()
                .requestMatchers("/**", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/login", "/logout", "/member/register", "/admin/register").permitAll()
                .requestMatchers("/member/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/admin/**", "/member/**").hasRole("ADMIN");

        //http.exceptionHandling(exceptionHandling ->exceptionHandling
        //         .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        // );

        //일반회원 로그인
        http.formLogin(login -> login
                .defaultSuccessUrl("/member/list", true)
                .failureUrl("/login?error=true")
                .loginPage("/member/login")
                .usernameParameter("userid") //entity에 아이디 필드명
                .permitAll()
                .successHandler(new CustomLoginSuccessHandler()));

        //CSRF 보호를 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        //로그아웃
        http.logout(logout-> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/member/login")); //로그아웃

        //일반 사용자인 경우 일반 사용자로그인 처리
        http.authenticationProvider(memberProvider());
        return http.build();
    }


//    @Bean
//    @Order(3)
//    public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {
//        //사용권한
//        http.securityMatcher("/manager/**").authorizeRequests()
//                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
//                .requestMatchers("/h2-console/**").permitAll()
//                .requestMatchers("/manager/login").permitAll()
//                .requestMatchers("/login", "/logout", "/member/register").permitAll()
//                .requestMatchers("/member/**").hasAnyRole("ADMIN", "USER", "MANAGER")
//                .requestMatchers("/admin/**", "/member/**").hasRole("ADMIN");
//
//        //관리자회원 로그인
//        http.formLogin(login -> login
//                .defaultSuccessUrl("/manager/list", true)
//                .failureUrl("/manager/login?error=true")
//                .loginPage("/manager/login")
//                .usernameParameter("managerid") //entity에 아이디 필드명
//                .permitAll()
//                .successHandler(new CustomLoginSuccessHandler()));
//
//        //CSRF 보호를 비활성화
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        //로그아웃
//        http.logout(logout-> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/manager/login")); //로그아웃
//
//        //관리자인 경우 관리자 로그인처리
//        http.authenticationProvider(adminProvider());
//
//        return http.build();
//    }
}