package com.star.bf.service;

import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;
import com.star.bf.config.BloomFilterConfig;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @Author: zzStar
 * @Date: 05-26-2021 14:53
 */
@Service
public class BloomFilter<T> {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private BloomFilterConfig bloomFilterConfig;

    /**
     * 预计数位
     */
    private long numBits;

    /**
     * 哈希函数数量
     */
    private int numHashFunctions;

    /**
     * Bloom 过滤器中元素数量
     */
    private long count;

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

    @PostConstruct
    private void postConstruct() {
        init(null, null);
    }

    /**
     * init
     *
     * @param expectedInsertions 预期要插入的大小
     * @param fpp                容错率
     */
    private void init(Long expectedInsertions, Float fpp) {
        fpp = fpp == null ? bloomFilterConfig.getFpp() : fpp;
        expectedInsertions = expectedInsertions == null ? bloomFilterConfig.getExpectedInsertions() : expectedInsertions;
        checkArgument(expectedInsertions >= 0, "Expected insertions (%s) must be >= 0", expectedInsertions);
        checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", fpp);
        checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", fpp);
        // bit 位的数量
        this.numBits = optimalNumOfBits(expectedInsertions, fpp);
        // 哈希函数的数量
        this.numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
    }

    /**
     * 重新加载布隆过滤器，会清空原来的布隆过滤器
     *
     * @param keys keys
     */
    public void reload(List<T> keys) {
        new Thread(() -> {
            this.delete();
            init((long) keys.size(), 0.01f);
            for (T key : keys) {
                this.put(key);
            }
        }).start();
    }

    /**
     * 删除 Bloom 过滤器
     */
    public void delete() {
        redisTemplate.delete(bloomFilterConfig.getFilterName());
    }

    /**
     * key 是否存在
     *
     * @param key key
     */
    public boolean exist(T key) {
        long[] indexes = getIndexes(key);
        List<Boolean> list = redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            redisConnection.openPipeline();
            for (long index : indexes) {
                redisConnection.getBit(bloomFilterConfig.getFilterName().getBytes(), index);
            }
            redisConnection.close();
            return null;
        });
        return !list.contains(false);
    }

    /**
     * 添加
     *
     * @param keys keys
     */
    public void put(T... keys) {
        for (T key : keys) {
            long[] indexes = getIndexes(key);
            redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
                redisConnection.openPipeline();
                for (long index : indexes) {
                    redisConnection.setBit(bloomFilterConfig.getFilterName().getBytes(), index, true);
                }
                count++;
                redisConnection.close();
                return null;
            });
        }
    }

    /**
     * Bloom 过滤器中元素的数量
     */
    public long count() {
        return count;
    }

    /**
     * Bloom 过滤器中位为 true 的数量
     */
    public Long bitCount() {
        Object object = redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            Long result = redisConnection.bitCount(bloomFilterConfig.getFilterName().getBytes());
            redisConnection.close();
            return result;
        });
        return object == null ? 0L : (Long) object;
    }

    /**
     * 根据 key 获取下标
     */
    private long[] getIndexes(T key) {
        long hash1 = hash(key);
        long hash2 = hash1 >>> 16;
        long[] result = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            long combinedHash = hash1 + i * hash2;
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            // 对哈希值取余，保证不超过 bit 位的长度
            result[i] = combinedHash % numBits;
        }
        return result;
    }

    private long hash(T key) {
        Charset charset = StandardCharsets.UTF_8;
        return Hashing.murmur3_128().hashObject(key.toString(), Funnels.stringFunnel(charset)).asLong();
    }

    static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    static int optimalNumOfHashFunctions(long n, long m) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

}
