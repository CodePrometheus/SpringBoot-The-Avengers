package com.star.service.impl;

import com.star.domain.LogEntity;
import com.star.service.OperationLogService;
import com.star.thread.OperationLogThread;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: zzStar
 * @Date: 04-08-2021 15:27
 */
@Component
public class DefaultLogHandler extends LogHandlerImpl<LogEntity> {

    @Resource
    private OperationLogService operationLogService;

    public DefaultLogHandler(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        super(threadPoolTaskExecutor);
    }


    @Override
    public void persistenceLog(LogEntity logEntity) throws Exception {
        this.threadPoolTaskExecutor.execute(new OperationLogThread(operationLogService, logEntity));
    }

}
