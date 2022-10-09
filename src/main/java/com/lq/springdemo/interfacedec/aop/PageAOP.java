package com.lq.springdemo.interfacedec.aop;

import com.lq.springdemo.entity.common.Page;
import com.lq.springdemo.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Slf4j
public class PageAOP {
    @Around("@annotation(com.lq.springdemo.interfacedec.ComPage)")
    public Object pageAopAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Page page = PageUtils.getPage();
        if (page != null) {
            page.setEnable(true);
        }
        try {
            //执行目标方法
            Object result = joinPoint.proceed();
            return result;
        } finally {
            if (page != null) {
                page.setEnable(false);
            }
        }
    }
//    @Before("@annotation(com.lq.springdemo.interfacedec.Page)")
//    public Object pageAOP(JoinPoint joinPoint) throws Throwable {
//        log.info("进入注解@page连接点，设置分页线程对象为启用");
//        Page page = PageUtils.getPage();
//        if (page != null) {
//            page.setEnable(true);
//        }
//        log.info(joinPoint.getKind());
//        try {
////            System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
////            System.out.println("目标方法所属类的简单类名:" +        joinPoint.getSignature().getDeclaringType().getSimpleName());
////            System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
////            System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
//            //获取传入目标方法的参数
//            Object[] args = joinPoint.getArgs();
//            for (int i = 0; i < args.length; i++) {
//                System.out.println("第" + (i+1) + "个参数为:" + args[i]);
//            }
////            System.out.println("被代理的对象:" + joinPoint.getTarget());
////            System.out.println("代理对象自己:" + joinPoint.getThis());
//            log.info("@page连接点，开始执行方法");
//            return joinPoint.getThis();
//        } finally {
//            if (page != null) {
//                page.setEnable(false);
//                log.info("@page连接点，设置分页线程对象为禁用");
//            }
//        }
//    }
}
