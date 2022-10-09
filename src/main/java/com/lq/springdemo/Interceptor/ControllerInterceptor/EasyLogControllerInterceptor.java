package com.lq.springdemo.Interceptor.ControllerInterceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;

@Slf4j
@Component
public class EasyLogControllerInterceptor implements HandlerInterceptor {

    /**
     * 在Controller中调用前执行拦截
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(request.getRequestURI()+"在Controller中调用前执行拦截");
        return true;
    }

    /**
     *在Controller中调用中执行拦截
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod h = (HandlerMethod) handler;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            log.info("用户想执行的操作是:" + h.getMethod().getName() + "开始时间" + df.format(System.currentTimeMillis()));
        }
        log.info(request.getRequestURI()+"在Controller中调用中执行拦截");
    }

    /**
     * 在Controller中调用后执行拦截
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info(request.getRequestURI()+"在Controller中调用后执行拦截");
    }
}
