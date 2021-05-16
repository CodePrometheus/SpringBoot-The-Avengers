package com.star.nginx.service;

import com.alibaba.fastjson.JSONObject;
import com.star.nginx.algorithm.ConsistentHash;
import com.star.nginx.algorithm.LoadBalanceAlgorithm;
import com.star.nginx.algorithm.RoundRobin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.star.nginx.algorithm.LoadBalanceAlgorithm.ipList;

/**
 * @Author: zzStar
 * @Date: 05-16-2021 10:34
 */
@Service
public class LoadBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalanceService.class);

    @Resource
    private RoundRobin roundRobin;

    @Resource
    private ConsistentHash consistentHash;

    @Value("${load.balance.algorithm}")
    private String loadBalance;

    /**
     * 选择负载均衡策略，默认是轮训都方式
     *
     * @return
     */
    private LoadBalanceAlgorithm choseAlgorithm() {
        logger.info("负载均衡策略：{}", loadBalance);
        if (Objects.equals("ConsistentHash", loadBalance)) {
            return consistentHash;
        }
        return roundRobin;
    }

    /**
     * 选择服务器IP
     *
     * @param invocation
     * @return
     */
    public String choseServerIp(String invocation) {
        return choseAlgorithm().choseServiceIp(invocation);
    }

    /**
     * 添加服务器地址
     *
     * @param ips
     */
    public void addIps(List<String> ips) {
        choseAlgorithm().saveIpsWithoutWeight(ips);
    }

    /**
     * 删除服务地址
     *
     * @param deleteAll
     * @param ips
     */
    public void removeIp(Boolean deleteAll, List<String> ips) {
        if (deleteAll) {
            choseAlgorithm().removeAllIps();
        } else {
            ips.forEach(ip -> choseAlgorithm().removeIp(ip));
            logger.info("ipList: {}", JSONObject.toJSONString(ipList));
        }
    }

    /**
     * 查询服务器列表
     *
     * @return
     */
    public Map<String, Integer> queryIpList() {
        return ipList;
    }

}
