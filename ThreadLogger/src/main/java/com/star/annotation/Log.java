package com.star.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @Author: zzStar
 * @Date: 04-08-2021 15:11
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 操作描述
     */
    String value() default "";

    /**
     * 是否持久化日志
     */
    boolean persistent() default true;

}
