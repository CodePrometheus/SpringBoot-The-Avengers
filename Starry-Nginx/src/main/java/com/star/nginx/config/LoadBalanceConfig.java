package com.star.nginx.config;

import com.star.nginx.algorithm.ConsistentHash;
import com.star.nginx.algorithm.RoundRobin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zzStar
 * @Date: 05-16-2021 11:05
 */
@Configuration
public class LoadBalanceConfig {

    @Bean(name = "consistentHash")
    ConsistentHash consistentHash() {
        return new ConsistentHash();
    }

    @Bean(name = "roundRobin")
    RoundRobin roundRobin() {
        return new RoundRobin();
    }
}
