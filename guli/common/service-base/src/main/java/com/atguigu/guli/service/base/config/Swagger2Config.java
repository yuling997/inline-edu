package com.atguigu.guli.service.base.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket adminApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .apiInfo(adminApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/admin/.*")))
                .build();
    }

    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder().title("后台管理系统API文档")
                .description("本文描述了在线教育后台管理系统课程中心的微服务接口")
                .version("2.5.6")
                .contact(new Contact("ZYL","http://atguigu.com","2872667714@qq.com"))
                .build();
    }

    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.and(PathSelectors.regex("/web/.*")))
                .build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder().title("网站API文档")
                .description("本文描述了在线教育网站课程中心的微服务接口")
                .version("2.5.6")
                .contact(new Contact("ZYL","http://atguigu.com","2872667714@qq.com"))
                .build();
    }
}
