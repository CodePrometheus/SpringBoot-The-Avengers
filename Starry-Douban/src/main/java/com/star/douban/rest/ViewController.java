package com.star.douban.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: zzStar
 * @Date: 05-15-2021 10:49
 */
@Controller
public class ViewController {


    @GetMapping("/")
    public String index() {
        return "index";
    }
}
