package com.star;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.HashMap;

@SpringBootTest
class JwtApplicationTests {

    @Test
    void contextLoads() {

        HashMap<String, Object> map = new HashMap<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 20);

        String token = JWT.create()
                .withHeader(map)//header
                .withClaim("userId", 21)//payload
                .withClaim("userName", "xiaohei")
                .withExpiresAt(instance.getTime())//令牌过期时间
                .sign(Algorithm.HMAC256("!@#$%￥"));//签名
        System.out.println(token);
    }

    @Test
    public void test() {
//        创建验证对象
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("!@#$%￥")).build();
        DecodedJWT verify = jwtVerifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTczMjcwNzAsInVzZXJJZCI6MjEsInVzZXJuYW1lIjoieGlhb2hlaSJ9.LNOl3XhbT90P-w3DNZ9rjFSb67rjayACG2Lo214qPXQ");
        System.out.println(verify.getClaim("userId").asInt());
        System.out.println(verify.getClaim("username").asString());
        System.out.println("过期时间" + verify.getExpiresAt());
    }

}
