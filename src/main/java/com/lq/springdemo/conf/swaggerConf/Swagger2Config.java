package com.lq.springdemo.conf.swaggerConf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger API 文档的配置
 * 集成到 SpringBoot 中时，要放在 SpringBoot 启动类 同级的目录下
 */
@Configuration
@EnableOpenApi
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true) // 默认开启
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lq.springdemo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("HealChow Swagger Doc")
                .description("更多请关注：《https://healchow.com》")
                .version("1.0")
                .build();
    }
}
