package com.star.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description: swagger3使用
 * @Author: zzStar
 * @Date: 03-13-2021 08:55
 */
@SpringBootApplication
@Slf4j
public class SwaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerApplication.class, args);
        log.info("=== Swagger3 Demo ===");
    }

}
