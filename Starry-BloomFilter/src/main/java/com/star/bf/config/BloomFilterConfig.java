package com.star.bf.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: zzStar
 * @Date: 05-26-2021 15:55
 */
@Data
@Component
public class BloomFilterConfig {

    /**
     * 过滤器名称
     */
    @Value("${bloom.filter-name}")
    private String filterName;

    /**
     * 容错率
     */
    @Value("${bloom.fpp}")
    private float fpp;

    /**
     * 预加载期望值
     */
    @Value("${bloom.expected-insertions}")
    private long expectedInsertions;

}
