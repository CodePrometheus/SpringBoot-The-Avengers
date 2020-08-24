package com.star;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

}
