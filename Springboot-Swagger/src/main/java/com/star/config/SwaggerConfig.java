package com.star.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket creatRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.star.controller"))
                .build().apiInfo(new ApiInfoBuilder()
                        .title("标题：Springboot整合Swagger入门")
                        .description("火影忍者 --- 无限月读")
                        .version("版本2.0")
                        .contact(new Contact("nanduo", "http://www.ningren.jp", "www.sasigi.jp"))
                        .build());
    }

}
