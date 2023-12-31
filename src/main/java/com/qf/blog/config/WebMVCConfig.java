package com.qf.blog.config;

//import com.qf.blog.inteceprot.LoginUserInterceprot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginUserInterceprot loginUserInterceprot;

    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginUserInterceprot).addPathPatterns("/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 访问/toLogin就会跳转到login的视图
        // 这样写的每个页面都需要建立一个映射关系
//        registry.addViewController("/toLogin").setViewName("site/login");
    }
}


