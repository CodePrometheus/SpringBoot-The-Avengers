package com.star.service.impl;

import com.alibaba.fastjson.JSON;
import com.star.domain.LogEntity;
import com.star.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 如果一个接口有多个实现类，优先使用@Primary标注的类
 *
 * @Author: zzStar
 * @Date: 04-08-2021 15:33
 */
@Service
@Primary
public class OperationLogServiceImpl implements OperationLogService {

    private static final Logger log = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Override
    public void saveLog(LogEntity logEntity) {
        log.info("访问日志", JSON.toJSONString(logEntity));
    }

}
