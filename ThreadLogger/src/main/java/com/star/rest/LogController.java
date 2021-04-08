package com.star.rest;

import com.star.annotation.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zzStar
 * @Date: 04-08-2021 17:39
 */
@RestController
public class LogController {

    @Log
    @GetMapping("test")
    public String test() {
        System.out.println("test...log record");
        return "success";
    }
}
