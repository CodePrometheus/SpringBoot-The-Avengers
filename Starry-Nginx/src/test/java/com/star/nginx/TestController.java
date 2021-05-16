package com.star.nginx;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.AtomicLongMap;
import com.star.nginx.service.LoadBalanceService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: zzStar
 * @Date: 05-16-2021 11:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestController {

    @Resource
    private LoadBalanceService loadBalanceService;

    @Test
    public void addIps() {
        List<String> ips = new ArrayList<>();
        ips.add("192.168.3.1");
        ips.add("192.168.3.2");
        ips.add("192.168.3.3");
        ips.add("192.168.3.4");
        ips.add("192.168.3.5");
        ips.add("192.168.3.6");
        ips.add("192.168.3.7");
        loadBalanceService.addIps(ips);
        Map<String, Integer> ipList = loadBalanceService.queryIpList();
        System.out.println(ipList);
        Assert.assertEquals(ipList.size(), 7);
    }

    @Test
    public void testDeleteIp() {
        List<String> ips = new ArrayList<>();
        ips.add("192.168.3.1");
        ips.add("192.168.3.2");
        ips.add("192.168.3.3");
        ips.add("192.168.3.4");
        ips.add("192.168.3.5");
        ips.add("192.168.3.6");
        ips.add("192.168.3.7");
        List<String> deleteIps = new ArrayList<>();
        deleteIps.add("192.168.3.6");
        deleteIps.add("192.168.3.7");
        loadBalanceService.addIps(ips);
        loadBalanceService.removeIp(false, deleteIps);
        Map<String, Integer> result = loadBalanceService.queryIpList();
        Assert.assertEquals(result.size(), 5);
    }

    @Test
    public void testChoseServerIp() {
        List<String> ips = new ArrayList<>();
        ips.add("192.168.3.1");
        ips.add("192.168.3.2");
        ips.add("192.168.3.3");
        ips.add("192.168.3.4");
        ips.add("192.168.3.5");
        ips.add("192.168.3.6");
        ips.add("192.168.3.7");
        loadBalanceService.addIps(ips);


        AtomicLongMap<String> result = AtomicLongMap.create();
        ips.forEach(ip -> result.put(ip, 0));

        for (int i = 0; i < 1000; i++) {
            String ip = loadBalanceService.choseServerIp(UUID.randomUUID().toString());
            result.getAndIncrement(ip);
        }
        System.out.println(JSONObject.toJSONString(result.asMap()));
    }


}
