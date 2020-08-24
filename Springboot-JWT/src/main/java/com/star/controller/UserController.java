package com.star.controller;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.star.entity.User;
import com.star.service.UserService;
import com.star.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/login")
    public Map<String, Object> login(User user) {
        log.info("用户名：[{}] ", user.getName());
        log.info("密码：[{}] ", user.getPassword());
        Map<String, Object> map = new HashMap<>();
        try {
            User login = userService.login(user);
//            在此生成JWT令牌
            Map<String, String> payload = new HashMap<>();
            payload.put("id", login.getId());
            payload.put("name", login.getName());
            String token = JWTUtils.getToken(payload);
            map.put("token", token);//响应token，存储在客户端，每次请求时携带
            map.put("state", true);
            map.put("msg", "成功登录");
        } catch (Exception e) {
            map.put("state", false);
            map.put("msg", "认证失败");
        }
        return map;
    }

    @PostMapping("/user/test")
    public Map<String, Object> teat(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
//        处理业务逻辑
//        可以通过request把头里的token拿出来
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        log.info("用户id:[{}]", verify.getClaim("id").asInt());
        log.info("用户name:[{}]", verify.getClaim("name").asString());
        map.put("state", true);
        map.put("msg", "请求成功");
        return map;
    }

}
