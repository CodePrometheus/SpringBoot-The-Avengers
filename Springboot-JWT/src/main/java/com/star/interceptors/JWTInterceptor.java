package com.star.interceptors;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.utils.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//拦截器
public class JWTInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        获取请求头中的令牌
        String token = request.getHeader("token");

        Map<String, Object> map = new HashMap<>();
//        验证token
        try {
            DecodedJWT verify = JWTUtils.verify(token);
            return true;//放行请求
//            可对异常做细腻度处理
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            map.put("msg", "无效签名");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token过期");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "算法不一致");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "token无效");
        }
        map.put("state", false);//设置状态
//        将map转为JSON响应给前台
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
