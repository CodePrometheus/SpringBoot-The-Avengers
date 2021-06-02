package com.star.elk.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zzStar
 * @Date: 06-02-2021 16:45
 */
@Slf4j
@RestController
public class ElkTestController {


    @GetMapping("log")
    public void log() {
        log.info("elk test");
    }

}
