package com.star.nginx.rest;

import com.star.nginx.domain.Ip;
import com.star.nginx.service.LoadBalanceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: zzStar
 * @Date: 05-16-2021 11:05
 */
@RestController
public class LoadBalanceController {

    @Resource
    private LoadBalanceService loadBalanceService;

    /**
     * 添加服务器地址
     *
     * @param ip
     */
    @PostMapping("add/{ip}")
    public void addIps(@Validated(Ip.AddIps.class) @PathVariable Ip ip) {
        loadBalanceService.addIps(ip.getServerIps());
    }

    @DeleteMapping("del")
    public void deleteIps(@Validated(Ip.DeleteIps.class) Ip ip) {
        loadBalanceService.removeIp(ip.getDeleteAllFlag(), ip.getServerIps());
    }
}
