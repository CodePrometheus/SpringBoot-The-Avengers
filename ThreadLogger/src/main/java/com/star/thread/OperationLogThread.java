package com.star.thread;

import com.alibaba.fastjson.JSON;
import com.star.domain.LogEntity;
import com.star.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 记录日志线程
 *
 * @Author: zzStar
 * @Date: 04-08-2021 15:39
 */
public class OperationLogThread implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(OperationLogThread.class);

    /**
     * 可见性：就是指当一个线程修改了线程共享变量的值，其它线程能够立即得知这个修改
     * 当一个共享变量被volatile修饰时，它会保证修改的值会立即被更新到主存，当有其他线程需要读取时，它会去内存中读取新值。
     * 原子性：即一个操作或者多个操作 要么全部执行并且执行的过程不会被任何因素打断，要么就都不执行。。
     * 有序性：程序执行的顺序按照代码的先后顺序执行。
     */
    private volatile OperationLogService operationLogService;

    private volatile LogEntity logEntity;

    public OperationLogThread(OperationLogService operationLogService,
                              LogEntity logEntity) {
        this.operationLogService = operationLogService;
        this.logEntity = logEntity;
    }

    @Override
    public void run() {
        /**
         * 加了判断之后，只有当前的日志级别大于或等于系统设置的级别才会进行拼接
         * 并发下提高效率
         */
        try {
            if (log.isInfoEnabled()) {
                log.info("Thread name " + Thread.currentThread().getName() + " start save operateLog " + JSON.toJSONString(logEntity));
            }
            this.operationLogService.saveLog(logEntity);

            if (log.isInfoEnabled()) {
                log.info("Thread name " + Thread.currentThread().getName() + "save operateLog success");
            }
        } catch (Exception e) {
            log.error("Thread name " + Thread.currentThread().getName() + "save operateLog error", e);
        }
    }
}
