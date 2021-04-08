package com.star.service;

import com.star.domain.LogEntity;

/**
 * @Author: zzStar
 * @Date: 04-08-2021 15:32
 */
public interface OperationLogService {

    /**
     * 保存操作日志
     *
     * @param logEntity
     */
    void saveLog(LogEntity logEntity);
}
