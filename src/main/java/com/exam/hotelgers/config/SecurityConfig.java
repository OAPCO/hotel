package com.exam.hotelgers.config;

import com.exam.hotelgers.entity.BranchChief;
import com.exam.hotelgers.entity.Manager;
import com.exam.hotelgers.service.*;
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
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AdminLoginService adminLoginService() {
        return new AdminLoginService();
    }
    @Bean
    public MemberLoginService memberLoginService() {
        return new MemberLoginService();
    }
    @Bean
    public ManagerLoginService managerLoginService() {
        return new ManagerLoginService();
    }

    @Bean
    public DistChiefLoginService distChiefLoginService() {
        return new DistChiefLoginService();
    }

    @Bean
    public BranchChiefLoginService branchChiefLoginService() {
        return new BranchChiefLoginService();
    }


    @Bean
    public DaoAuthenticationProvider adminProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminLoginService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

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
    public DaoAuthenticationProvider distchiefProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(distChiefLoginService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public DaoAuthenticationProvider branchchiefProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(branchChiefLoginService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }




    @Bean
    @Order(1)
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {

        http.securityMatcher("/admin/**").authorizeRequests()
                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/admin/adminpage/codemange").hasAnyRole("ADMIN","DISTCHIEF","BRANCHCHIEF")
                .requestMatchers("/admin/adminpage/storedistmange").hasRole("DISTCHIEF")
                .requestMatchers("/admin/adminpage/storemembermange").hasRole("BRANCHCHIEF");

        http.formLogin(login -> login
                .defaultSuccessUrl("/", true)
                .failureUrl("/admin/login?error=true")
                .loginPage("/admin/login")
                .usernameParameter("adminid")
                .permitAll()
                .successHandler(new CustomLoginSuccessHandler()));

        http.csrf(AbstractHttpConfigurer::disable);

        http.logout(logout-> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/admin/login"));

        http.authenticationProvider(adminProvider());
        http.authenticationProvider(distchiefProvider());

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {

        http.securityMatcher("/manager/**").authorizeRequests()
                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll();

        http.formLogin(login -> login
                .defaultSuccessUrl("/", true)
                .failureUrl("/manager/login?error=true")
                .loginPage("/manager/login")
                .usernameParameter("managerid")
                .permitAll()
                .successHandler(new CustomLoginSuccessHandler()));

        http.csrf(AbstractHttpConfigurer::disable);

        http.logout(logout-> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/manager/login"));

        http.authenticationProvider(managerProvider());

        return http.build();
    }


    @Bean
    @Order(3)
    public SecurityFilterChain filterChain3(HttpSecurity http) throws Exception {
        //사용권한
        http.authorizeRequests()
                .requestMatchers("/**", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/login", "/logout", "/member/register", "/admin/register").permitAll()
                .requestMatchers("/member/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/admin/**", "/member/**").hasRole("ADMIN");

        http.formLogin(login -> login
                .defaultSuccessUrl("/", true)
                .failureUrl("/member/login?error=true")
                .loginPage("/member/login")
                .usernameParameter("userid")
                .permitAll()
                .successHandler(new CustomLoginSuccessHandler()));


        http.csrf(AbstractHttpConfigurer::disable);

        http.logout(logout-> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/member/login"));

        http.authenticationProvider(memberProvider());
        return http.build();
    }


//    @Bean
//    @Order(4)
//    public SecurityFilterChain filterChain4(HttpSecurity http) throws Exception {
//
//        http.securityMatcher("/branchchief/**").authorizeRequests()
//                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
//                .requestMatchers("/h2-console/**").permitAll()
//                .requestMatchers("/admin/login").permitAll()
//                .requestMatchers("/admin/login", "/logout", "/member/register", "/admin/register").permitAll();
//
//        http.formLogin(login -> login
//                .defaultSuccessUrl("/", true)
//                .failureUrl("/branchchief/login?error=true")
//                .loginPage("/branchchief/login")
//                .usernameParameter("branchchiefid")
//                .permitAll()
//                .successHandler(new CustomLoginSuccessHandler()));
//
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        http.logout(logout-> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/branchchief/login"));
//
//        http.authenticationProvider(branchchiefProvider());
//
//        return http.build();
//    }
//
//
//
//
//    @Bean
//    @Order(5)
//    public SecurityFilterChain filterChain5(HttpSecurity http) throws Exception {
//
//        http.securityMatcher("/distchief/**").authorizeRequests()
//                .requestMatchers("/", "/css/**", "/js/**", "/img/**", "/images/**").permitAll()
//                .requestMatchers("/h2-console/**").permitAll()
//                .requestMatchers("/admin/login").permitAll()
//                .requestMatchers("/admin/login", "/logout", "/member/register", "/admin/register").permitAll();
//
//        http.formLogin(login -> login
//                .defaultSuccessUrl("/", true)
//                .failureUrl("/distchief/login?error=true")
//                .loginPage("/distchief/login")
//                .usernameParameter("distchiefid")
//                .permitAll()
//                .successHandler(new CustomLoginSuccessHandler()));
//
//        http.csrf(AbstractHttpConfigurer::disable);
//
//        http.logout(logout-> logout
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/distchief/login"));
//
//        http.authenticationProvider(distchiefProvider());
//
//        return http.build();
//    }



}