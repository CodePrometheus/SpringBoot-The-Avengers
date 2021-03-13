package com.star.idempotent.interceptor;

import com.star.idempotent.annotation.Idempotent;
import com.star.idempotent.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 接口幂等性校验拦截器
 *
 * @Author: zzStar
 * @Date: 03-13-2021 10:00
 */
@Component
public class IdempotentInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    /**
     * 预处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;


        /**
         * 幂等性校验, 校验通过则放行, 校验失败则抛出异常, 并通过统一异常处理返回友好提示
         * 被Idempotent注解标记的扫描
         */
        Idempotent idempotent = handlerMethod.getMethod().getAnnotation(Idempotent.class);
        if (idempotent != null) {
            try {
                tokenService.checkToken(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 必须返回true,否则会被拦截一切请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
