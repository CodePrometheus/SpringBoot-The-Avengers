package com.star.idempotent.service.impl;

import com.star.idempotent.config.AjaxResult;
import com.star.idempotent.config.RedisService;
import com.star.idempotent.config.ResponseCode;
import com.star.idempotent.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author: zzStar
 * @Date: 03-13-2021 10:04
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisService redisService;

    private static final String TOKEN_NAME = "token";

    @Override
    public AjaxResult createToken() {
        // 通过UUID来生产token
        String string = UUID.randomUUID().toString();
        StringBuilder token = new StringBuilder();

        token.append("idempotent").append(string);
        redisService.setEx(token.toString(), token.toString(), 300L);
        return AjaxResult.success(token);
    }

    @Override
    public AjaxResult checkToken(HttpServletRequest request) {
        // 从请求头中获取token
        String token = request.getHeader(TOKEN_NAME);
        if (StringUtils.isBlank(token)) {
            // 如果请求头token为空就从参数中获取
            log.error("===请求头里没有token===");
            token = request.getParameter(TOKEN_NAME);
            // 如果都为空抛出参数异常的错误
            if (StringUtils.isBlank(token)) {
                // 没有携带token，抛异常，这里的异常需要全局捕获
                log.error("===token校验失败===");
                throw new RuntimeException(ResponseCode.ILLEGAL_ARGUMENT.getMsg());
            }
        }
        // token不存在，说明token已经被其他请求删除或者是非法的token 抛出请求重复异常
        if (!redisService.exists(token)) {
            log.error("====token不存在 已经被删除 或者是非法token====");
            throw new RuntimeException(ResponseCode.REPETITIVE_OPERATION.getMsg());
        }

        // 请求成功后，删除该token，保证幂等性
        boolean remove = redisService.remove(token);
        // token删除失败，说明token已经被其他请求删除 抛出请求重复异常
        if (!remove) {
            log.error("====token删除失败====");
            throw new RuntimeException(ResponseCode.REPETITIVE_OPERATION.getMsg());
        }
        return new AjaxResult(0, "校验成功", null);
    }
}
