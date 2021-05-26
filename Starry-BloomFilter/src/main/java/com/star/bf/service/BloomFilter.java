package com.star.bf.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 05-26-2021 14:53
 */
@Service
public class BloomFilter {

    @Resource
    private RedisTemplate redisTemplate;

    public Object addLuaBloomFilter(String filterName, List<String> values) {
        DefaultRedisScript<Boolean> addBloom = new DefaultRedisScript<>();
        addBloom.setScriptSource(new ResourceScriptSource(new ClassPathResource("add.lua")));
        addBloom.setResultType(Boolean.class);

        /**
         * 这里调用方法 execute(RedisScript<T> script, List<K> keys, Object... args)
         * 这里的keys 对于 lua脚本中的 KEY[i]  这个i跟集合大小有关
         * 这里的args 对于 lua脚本中的 ARGV[i] 这个i跟加入可变参数的个数有关
         */
        List<String> keyList = new ArrayList<>();
        keyList.add(filterName);
        keyList.add(values.get(0));
        Object execute = redisTemplate.execute(addBloom, values, filterName);
        return execute;
    }

    public Boolean existsLuaBloomFilter(String filterName, String value) {
        DefaultRedisScript<Boolean> bloomExists = new DefaultRedisScript<>();
        bloomExists.setScriptSource(new ResourceScriptSource(new ClassPathResource("exist.lua")));
        bloomExists.setResultType(Boolean.class);
        List<String> keyList = new ArrayList<>();
        keyList.add(filterName);
        keyList.add(value);
        Boolean result = (Boolean) redisTemplate.execute(bloomExists, keyList);
        return result;
    }
}
