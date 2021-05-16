package com.star.nginx.aop;

import com.star.nginx.algorithm.RoundRobin;
import com.star.nginx.domain.NginxException;
import com.star.nginx.service.LoadBalanceService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.star.nginx.algorithm.LoadBalanceAlgorithm.ipList;

/**
 * @Author: zzStar
 * @Date: 05-16-2021 10:31
 */
@Aspect
@Component
public class LoadBalance {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalance.class);

    @Resource
    private LoadBalanceService loadBalanceService;

    @Resource
    private RoundRobin roundRobin;

    /**
     * 负载均衡
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around(value = "execution(public * com.star.nginx.rest..*.*(..))")
    public Object loadBalance(ProceedingJoinPoint point) {
        // 请求地址
        Object result = null;
        long start = System.currentTimeMillis();
        String methodName = point.getSignature().getName();
        String serverIp = loadBalanceService.choseServerIp(UUID.randomUUID().toString());

        // 执行完方法的返回值：调用proceed()方法，触发切入点方法执行
        try {
            result = serverIp;
            HttpServletResponse response = null;

            // 如果转发请求失败，则尝试切换服务地址进行转发（上限三次）
            if (Objects.isNull(response) || !Objects.equals(response.getStatus(), 200)) {
                List<String> errorIps = new ArrayList<>();
                errorIps.add(serverIp);

                for (int i = 1; i <= 3; i++) {
                    String ip = roundRobin.choseServiceIp(UUID.randomUUID().toString());
                    // 转发
                    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    response = requestAttributes.getResponse();

                    // 校验结果
                    if (Objects.nonNull(response) && Objects.equals(response.getStatus(), 200)) {
                        break;
                    } else {
                        errorIps.add(ip);
                    }
                    if (Objects.equals(i, 3)) {
                        throw new NginxException("服务器网络异常");
                    }
                }

                // 对所有发生问题的服务进行ping操作，如果有问题则从服务器列表中移除
                errorIps.stream()
                        .filter(ip -> !Objects.equals(ip, "127.0.0.1"))
                        .forEach(ip -> {
                            if (isReachable(ip, 300)) {
                                errorIps.remove(ip);
                            } else {
                                ipList.remove(ip);
                                logger.error("{} -> 服务器无法连接", ip);
                            }
                        });
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        long end = System.currentTimeMillis();
        logger.info("方法 {} 被调用， 所花费的时间 : {}", methodName, (end - start));
        return result;
    }

    /**
     * 模拟PING
     * 利用InetAddress的isReachable方法可以实现ping的功能，里面参数设定超时时间，返回结果表示是否连上
     *
     * @param ip      地址
     * @param timeOut 超时时间
     */
    private Boolean isReachable(String ip, int timeOut) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address.isReachable(timeOut);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
