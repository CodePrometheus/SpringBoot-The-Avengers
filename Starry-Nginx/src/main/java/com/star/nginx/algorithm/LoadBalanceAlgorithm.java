package com.star.nginx.algorithm;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zzStar
 * @Date: 05-16-2021 09:49
 */
@Component
public abstract class LoadBalanceAlgorithm {

    /**
     * ip列表，<地址,权重>
     */
    public static Map<String, Integer> ipList = new HashMap<>();

    /**
     * 存入IP地址,无权重配置,默认权重都为0
     *
     * @param ips
     */
    public void saveIpsWithoutWeight(List<String> ips) {
        ips.forEach(ip -> ipList.put(ip, 0));
    }

    /**
     * 移除所有服务器地址
     */
    public void removeAllIps() {
        ipList.clear();
    }

    public void removeIp(String ip) {
        ipList.remove(ip);
    }

    /**
     * 选择服务器ip
     *
     * @param invocation
     * @return
     */
    public abstract String choseServiceIp(String invocation);

}
