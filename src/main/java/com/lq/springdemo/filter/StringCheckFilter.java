package com.lq.springdemo.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *  请求过滤器
 */
@Slf4j
@Component
@WebFilter(filterName = "StringFilter", urlPatterns = "*")
public class StringCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("过滤器初始化，并加载~");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("请求被过滤拦截~");
        String path = ((HttpServletRequest) servletRequest).getRequestURI();
        log.info("请求地址："+path);
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest,servletResponse);
        long endTime = System.currentTimeMillis();
        log.info("该请求"+path+"执行时间:"+String.valueOf((endTime-startTime)/1000)+"s");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("过滤器销毁");
    }
}
