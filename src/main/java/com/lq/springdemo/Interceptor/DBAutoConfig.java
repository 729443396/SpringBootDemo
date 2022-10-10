package com.lq.springdemo.Interceptor;

import com.lq.springdemo.Interceptor.mybatisPlugins.PagePlugins;
import com.lq.springdemo.Interceptor.mybatisPlugins.ShowSqlLogInterceptor;
import com.lq.springdemo.interfacedec.aop.PageAOP;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan("com.lq.springdemo.mapperInterface")
@EnableTransactionManagement
public class DBAutoConfig {

    @Bean
    @ConditionalOnProperty(value = "mybatis.showSql", havingValue = "true", matchIfMissing = false)
    public ShowSqlLogInterceptor getPrintSQLPlugins(){
        return new ShowSqlLogInterceptor();
    }

    @Bean
    public PagePlugins getPagePlugins(){
        return  new PagePlugins();
    }

    @Bean
    public PageAOP getPageAOP(){
        return new PageAOP();
    }
}
