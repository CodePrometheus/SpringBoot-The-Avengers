package com.star.nginx.algorithm;

import com.star.nginx.domain.NginxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 轮询
 *
 * @Author: zzStar
 * @Date: 05-16-2021 10:28
 */
public class RoundRobin extends LoadBalanceAlgorithm {

    /**
     * 轮训指针
     */
    private static Integer pos = 0;

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

        String ip;
        synchronized (pos) {
            if (pos >= keySet.size()) {
                pos = 0;
            }
            ip = keyList.get(pos);
            pos++;
        }
        return ip;
    }
}
