package com.star.bf.rest;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zzStar
 * @Date: 05-26-2021 15:08
 */
@Slf4j
@RestController
public class CompareController {

    public static final int SIZE = 5000000;

    public static List<String> list = Lists.newArrayListWithCapacity(SIZE);

    public static Map<String, Integer> map = Maps.newHashMapWithExpectedSize(SIZE);

    /**
     * guava
     */
    BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.unencodedCharsFunnel(), SIZE);

    public static List<String> exist = Lists.newArrayList();

    public static Stopwatch stopWatch = Stopwatch.createUnstarted();

    @PostConstruct
    public void insertData() {
        for (int i = 0; i < SIZE; i++) {
            String data = UUID.randomUUID().toString();
            data = data.replace("-", "");
            list.add(data);
            map.put(data, 0);
            bloomFilter.put(data);
            if (i % 1000000 == 0) {
                exist.add(data);
            }
        }
    }

    @GetMapping("list")
    public void existList() {
        stopWatch.start();
        for (String s : exist) {
            if (list.contains(s)) {
                log.info("list集合存在该数据=============数据{}", s);
            }
        }
        stopWatch.stop();
        log.info("list集合测试，判断该元素集合中是否存在用时:{}", stopWatch.elapsed(TimeUnit.MILLISECONDS));
        stopWatch.reset();
    }

    /**
     * 查看map 判断k值是否存在
     */
    @GetMapping("map")
    public void existsMap() {
        //计时开始
        stopWatch.start();
        for (String s : exist) {
            if (map.containsKey(s)) {
                log.info("map集合存在该数据=============数据{}", s);
            }
        }
        //计时结束
        stopWatch.stop();
        //获取时间差
        log.info("map集合测试，判断该元素集合中是否存在用时:{}", stopWatch.elapsed(TimeUnit.MILLISECONDS));
        stopWatch.reset();
    }

    /**
     * 查看guava布隆过滤器 判断value值是否存在 执行时间
     */
    @GetMapping("bloom")
    public void existsBloom() {
        //计时开始
        stopWatch.start();
        for (String s : exist) {
            if (bloomFilter.mightContain(s)) {
                log.info("guava布隆过滤器存在该数据=============数据{}", s);
            }
        }
        //计时结束
        stopWatch.stop();
        //获取时间差
        log.info("bloom集合测试，判断该元素集合中是否存在用时:{}", stopWatch.elapsed(TimeUnit.MILLISECONDS));
        stopWatch.reset();
    }

}
