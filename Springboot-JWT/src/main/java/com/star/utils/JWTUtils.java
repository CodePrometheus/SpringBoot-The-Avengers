package com.star.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {

    public static final String SIGN = "!@#$%￥";

    //    生成token
    public static String getToken(Map<String, String> map) {

//        进行封装
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 30);

//        创建JWT builder
        JWTCreator.Builder builder = JWT.create();

//        payload
        map.forEach((k, v) -> {
            builder.withClaim(k, v);
        });

//        sign
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(SIGN));

        return token;
    }

    //    验证token合法性
    public static DecodedJWT verify(String token) {
//        对下面进行一个封装
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!@#$%￥")).build();
//        DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTczMTgxNDcsInVzZXJJZCI6MjEsInVzZXJuYW1lIjoieGlhb2hlaSJ9.04u5SaEgbw7lTxBiVbJRduY0b4aKCTXPJAi7xC8xW2c\n");
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
    }

//    获取token信息的方法
//    public static DecodedJWT getTokenInfo(String token){
//        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
//        return verify;
//    }

}
