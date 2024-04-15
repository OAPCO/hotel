package com.exam.hotelgers.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    @Value("${project.uploadPath}")
    String uploadPath;
    @Value("${imgLocation3}")
    String imgLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/images/**").addResourceLocations("file:///"+uploadPath);
        registry.addResourceHandler("/img/**").addResourceLocations("file:///"+imgLocation);
    }
}
