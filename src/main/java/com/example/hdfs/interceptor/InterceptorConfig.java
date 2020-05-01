package com.example.hdfs.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //此处配置拦截路径
        registry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
        //registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
    }
}
