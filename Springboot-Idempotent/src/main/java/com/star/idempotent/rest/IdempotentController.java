package com.star.idempotent.rest;

import com.star.idempotent.annotation.Idempotent;
import com.star.idempotent.config.AjaxResult;
import com.star.idempotent.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zzStar
 * @Date: 03-13-2021 10:39
 */
@RestController
@RequestMapping("token")
public class IdempotentController {

    @Autowired
    private TokenService tokenService;

    @GetMapping
    public AjaxResult token() {
        return tokenService.createToken();
    }

    @Idempotent
    @PostMapping("check")
    public AjaxResult checkToken() {
        return AjaxResult.success("Test Idempotent");
    }

}
