package com.lq.springdemo.Interceptor.mybatisPlugins;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Statement;


@Slf4j
@Intercepts({
        @Signature(
                type = StatementHandler.class,
                method = "query",
                args = {Statement.class, ResultHandler.class}
        ),
        @Signature(
                type = StatementHandler.class,
                method = "update",
                args = {Statement.class}
        )
})
@Component
public class ShowSqlLogInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        StatementHandler statementHandler= (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        log.info("----------------------------【SQL】-------------------------------");
        log.info(sql.replace("\n",""));
        long beginTime = System.currentTimeMillis();
        Object proceed = invocation.proceed(); // 放行，执行目标对象的对应方法
        long endTime = System.currentTimeMillis();
        log.info("----------------------------【SQL执行的时长为：{0} s】", BigDecimal.valueOf(endTime - beginTime).divide(BigDecimal.valueOf(1000)).setScale(6, RoundingMode.DOWN).doubleValue());
        return proceed;
    }

}
