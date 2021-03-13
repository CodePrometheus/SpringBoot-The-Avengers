package com.star.idempotent.service;

import com.star.idempotent.config.AjaxResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zzStar
 * @Date: 03-13-2021 10:03
 */
public interface TokenService {

    /**
     * 创建token
     *
     * @return
     */
    AjaxResult createToken();

    /**
     * 校验token
     *
     * @param request
     * @return
     * @throws Exception
     */
    AjaxResult checkToken(HttpServletRequest request) throws Exception;

}
