package com.lq.springdemo.conf.applicationCon;


import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@ComponentScan
public class ApplicationConfig extends WebMvcConfigurationSupport implements ServletContextInitializer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 下面定义了拦截器，会导致 spring.resources.static-locations 配置失效
        registry.addResourceHandler("/**").addResourceLocations("classpath:/webapp/public/");

        // 配置 knife4j 文档资源的访问路径
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            // 设置ThreadLocal拦截器
            registry.addInterceptor(threadLocalInterceptor()).addPathPatterns("/**")
                    .excludePathPatterns("/login/**").excludePathPatterns("/");

            super.addInterceptors(registry);
        } catch (Exception e) {

        }
    }

    private HandlerInterceptor threadLocalInterceptor() {
        return new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

    }
}
