package com.star.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author: zzStar
 * @Date: 04-08-2021 17:32
 */
@Configurable
@PropertySource(value = {"classpath:log.properties"})
public class LogConfig {

    @Value("${log.thread.corePoolSize}")
    private int corePoolSize;

    @Value("${log.thread.maxPoolSize}")
    private int maxPoolSize;

    @Value("${log.thread.prefix}")
    private String namePrefix;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setThreadNamePrefix(namePrefix);
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 用来设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        return threadPoolTaskExecutor;
    }
}
