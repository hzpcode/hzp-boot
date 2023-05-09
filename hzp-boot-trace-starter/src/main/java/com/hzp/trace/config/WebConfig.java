package com.hzp.trace.config;

import com.hzp.trace.interceptor.LogTraceInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author Yu
 * @date 2020/04/21 21:55
 **/

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogTraceInterceptor()).addPathPatterns("/**");
    }
}
