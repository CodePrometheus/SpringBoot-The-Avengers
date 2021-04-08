package com.star.handler;

/**
 * 日志处理接口
 *
 * @Author: zzStar
 * @Date: 04-08-2021 15:19
 */
public interface LogHandler<T> {

    /**
     * 处理日志
     *
     * @param t
     * @param isPersistent
     * @throws Exception
     */
    void processLog(T t, boolean isPersistent) throws Exception;


    /**
     * 持久化日志
     *
     * @param t
     * @throws Exception
     */
    void persistenceLog(T t) throws Exception;

    /**
     * 是否需要持久化日志
     *
     * @param isPersistent
     * @return
     */
    boolean customerWantsPersistenceLog(boolean isPersistent);
}
