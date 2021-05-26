package com.star.bf.rest;

import com.google.common.collect.Lists;
import com.star.bf.service.BloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 05-26-2021 15:23
 */
@Slf4j
@RestController
public class RedisBloomController {

    public static final String FILTER_NAME = "isMember";

    @Resource
    private BloomFilter bloomFilter;

    /**
     * 保存 数据到redis布隆过滤器
     */
    @GetMapping("save")
    public Object saveRedisBloom() {
        // 数据插入布隆过滤器
        List<String> exist = Lists.newArrayList("11111", "22222");
        Object object = bloomFilter.addLuaBloomFilter(FILTER_NAME, exist);
        log.info("保存是否成功====object:{}", object);
        return object;
    }

    /**
     * 查询 当前数据redis布隆过滤器是否存在
     */
    @GetMapping("exists")
    public void existsRedisBloom() {
        // 不存在输出
        if (!bloomFilter.existsLuaBloomFilter(FILTER_NAME, "00000")) {
            log.info("redis布隆过滤器不存在该数据=============数据{}", "00000");
        }
        // 存在输出
        if (bloomFilter.existsLuaBloomFilter(FILTER_NAME, "11111")) {
            log.info("redis布隆过滤器存在该数据=============数据{}", "11111");
        }
    }

}
