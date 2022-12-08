package com.example.quartz.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2Config {

    /**
     * @return
     */
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//定义api文档汇总信息
                .enable(true)
                .select().apis(RequestHandlerSelectors.basePackage("com.example.quartz.controller"))//指定扫描controller位置
                .paths(PathSelectors.any())//所有controller
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("api")//文档标题
                .contact(new Contact("GorryLee","","54564@gmail.com"))
                .description("自由飞翔，乱学怪学")//详细信息
                .version("1.0.1")//版本号
                .build();


    }
}
