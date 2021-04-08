package com.star.service.impl;

import com.star.handler.LogHandler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 实现LogHandler`
 *
 * @Author: zzStar
 * @Date: 04-08-2021 15:22
 */
public abstract class LogHandlerImpl<T> implements LogHandler<T> {

    protected ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public LogHandlerImpl(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Override
    public void processLog(T t, boolean isPersistent) throws Exception {
        if (customerWantsPersistenceLog(isPersistent)) {
            persistenceLog(t);
        }
    }

    /**
     * 由子类实现
     *
     * @param t
     * @throws Exception
     */
    @Override
    public abstract void persistenceLog(T t) throws Exception;

    @Override
    public boolean customerWantsPersistenceLog(boolean isPersistent) {
        return isPersistent;
    }
}
