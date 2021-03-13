package com.star.idempotent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * 奔跑吧 zzStar :)
 *
 * @Author: zzStar
 * @Date: 03-13-2021 10:56
 */
@SpringBootApplication
@Slf4j
public class IdempotentRunning {

    public static void main(String[] args) {
        SpringApplication.run(IdempotentRunning.class, args);
        log.info("=== Springboot 整合 Idempotent ===");
    }
}
