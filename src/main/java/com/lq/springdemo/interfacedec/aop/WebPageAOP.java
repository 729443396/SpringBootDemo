package com.lq.springdemo.interfacedec.aop;

import com.lq.springdemo.entity.common.BasePageResult;
import com.lq.springdemo.entity.common.Page;
import com.lq.springdemo.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class WebPageAOP {
    @Around("@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public Object pageAOP(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("执行-----------pageAOP--------------");
        // 获取参数
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        if (!StringUtils.isEmpty(pageNo) && !StringUtils.isEmpty(pageSize)) {
            PageUtils.setPage(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        }
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
            Page page = PageUtils.getPage();
            if (proceed instanceof BasePageResult && page != null) {
                BasePageResult basePageResult = (BasePageResult) proceed;
                basePageResult.setPage(page);
            }
        } catch (Throwable e) {
            throw e;
        } finally {
            PageUtils.clear();
        }
        return proceed;
    }
}
