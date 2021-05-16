package com.star.nginx.algorithm;

import com.star.nginx.domain.NginxException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性Hash
 *
 * @Author: zzStar
 * @Date: 05-16-2021 09:54
 */
public class ConsistentHash extends LoadBalanceAlgorithm {

    private static final long FNV_32_INIT = 2166136261L;
    private static final int FNV_32_PRIME = 16777619;

    private final static int VIRTUAL_NODE_SIZE = 10;
    private final static String VIRTUAL_NODE_SUFFIX = "&&";

    private static MessageDigest md5Digest;

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

    @Override
    public String choseServiceIp(String invocation) {
        if (ipList.isEmpty()) {
            throw new NginxException("地址为空");
        }

        Map<String, Integer> map = new ConcurrentHashMap<>();
        map.putAll(ipList);

        // ip存入list
        Set<String> keySet = map.keySet();
        List<String> keyList = new ArrayList<>();
        keyList.addAll(keySet);

        int invocationHashCode = getHashMurmur(invocation);
        TreeMap<Long, String> hashRing = buildConsistentHashRing(keyList);
        String ip = locate(hashRing, invocationHashCode);
        return ip;
    }

    /**
     * 使用FNV1_32_HASH算法计算服务器的Hash值
     *
     * @param origin
     * @return
     */
    private static int getHashFNV(String origin) {
        final int p = FNV_32_PRIME;
        int hash = (int) FNV_32_INIT;
        for (int i = 0; i < origin.length(); i++) {
            hash = (hash ^ origin.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        hash = Math.abs(hash);
        return hash;
    }

    /**
     * 使用MurmurHash算法计算服务器的Hash值
     *
     * @param origin
     * @return
     */
    private static int getHashMurmur(String origin) {
        ByteBuffer buf = ByteBuffer.wrap(origin.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }
        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return (int) (h & 0xffffffffL);
    }

    /**
     * 构建Hash环
     *
     * @param ips
     * @return
     */
    private TreeMap<Long, String> buildConsistentHashRing(List<String> ips) {
        TreeMap<Long, String> virtualNodeRing = new TreeMap<>();
        for (String server : ips) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE / 4; i++) {
                byte[] digest = computeMd5(server + VIRTUAL_NODE_SUFFIX + i);
                for (int h = 0; h < 4; h++) {
                    Long k = ((long) (digest[3 + h * 4] & 0xFF) << 24)
                            | ((long) (digest[2 + h * 4] & 0xFF) << 16)
                            | ((long) (digest[1 + h * 4] & 0xFF) << 8)
                            | (digest[h * 4] & 0xFF);
                    virtualNodeRing.put(k, server);

                }
            }
        }
        return virtualNodeRing;
    }

    private static byte[] computeMd5(String k) {
        MessageDigest md5;
        try {
            md5 = (MessageDigest) md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }
        md5.update(k.getBytes());
        return md5.digest();
    }

    /**
     * 获取服务器节点都ip
     *
     * @param ring
     * @param invocationHashCode
     * @return
     */
    private String locate(TreeMap<Long, String> ring, int invocationHashCode) {
        // 向右找到第一个 key
        Map.Entry<Long, String> locateEntry = ring.ceilingEntry((long) invocationHashCode);
        if (locateEntry == null) {
            // 想象成一个环，超过尾部则取第一个 key
            locateEntry = ring.firstEntry();
        }
        return locateEntry.getValue();
    }
}
