package com.star.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.star.annotation.Log;
import com.star.domain.LogEntity;
import com.star.handler.LogHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日志切面
 *
 * @Author: zzStar
 * @Date: 04-08-2021 15:13
 */
@Aspect
@Order(10)
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();
    private ThreadLocal<LogEntity> logEntityThreadLocal = new ThreadLocal<>();
    private ThreadLocal<Boolean> isPersistentThreadLocal = new ThreadLocal<>();
    public static final String TYPE_NAME_SERVLET = "org.springframework.security.web.servletapi.HttpServlet3RequestFactory$Servlet3SecurityContextHolderAwareRequestWrapper";
    public static final String UNDERTOW_SERVLET_TYPE_NAME = "io.undertow.servlet.spec.HttpServletRequestImpl";
    public static final String APACHE_REQUEST_FACADE = "org.apache.catalina.connector.RequestFacade";
    public static final String MOCK_HTTP_SERVLET_REQUEST = "org.springframework.mock.web.MockHttpServletRequest";

    @Resource
    private LogHandler logHandler;

    /**
     * 匹配有指定注解的方法（注解作用在方法上面）
     * 被调用的方法包含指定的注解
     */
    @Pointcut("@annotation(com.star.annotation.Log)")
    public void start() {
    }

    /**
     * 目标方法执行之前执行以下方法体的内容
     * Around ->@Before->主方法体->@Around中pjp.proceed()->@After->@AfterReturning
     * value：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     * joinPoint：提供对连接点处可用状态和有关它的静态信息的反射访问
     *
     * @param joinPoint
     */
    @Before("start()")
    public void aspectBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        LogEntity logEntity = new LogEntity();

        logEntity.setBrowserInfo(request.getHeader("User-Agent"));
        logEntity.setRequestURL(request.getRequestURL().toString());
        logEntity.setHttpMethod(request.getMethod());
        logEntity.setRequestIP(request.getRemoteAddr());

        // 获取防范输入参数
        Object[] args = joinPoint.getArgs();
        List<Object> paramList = new ArrayList<>();

        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    String typeName = args[i].getClass().getTypeName();
                    if (logger.isInfoEnabled()) {
                        logger.info(" request Parameter TypeName is " + typeName);
                    }
                    if (TYPE_NAME_SERVLET.equals(typeName) || UNDERTOW_SERVLET_TYPE_NAME.equals(typeName)
                            || APACHE_REQUEST_FACADE.equals(typeName) || MOCK_HTTP_SERVLET_REQUEST.equals(typeName)) {
                        continue;
                    }
                    paramList.add(args[i]);
                }
            }
            // List字段如果为null,输出为[],而非null
            String jsonString = JSON.toJSONString(paramList, SerializerFeature.WriteNullListAsEmpty);
            logEntity.setRequestParam(jsonString);
        }

        // 操作的类和方法
        logEntity.setOperateClassMethod(joinPoint.getSignature().getDeclaringTypeName() + "." +
                joinPoint.getSignature().getName());
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 获取注解中的value值
        Log log = method.getAnnotation(Log.class);
        // 操作描述
        logEntity.setOperateDesc(log.value());

        if (logger.isInfoEnabled()) {
            logger.info("请求参数: [ " + logEntity + " ]");
        }

        startTimeThreadLocal.set(System.currentTimeMillis());
        // 是否持久化
        isPersistentThreadLocal.set(log.persistent());

        // 日志对象存储到ThreadLocal对象下，方便对象传递
        logEntityThreadLocal.set(logEntity);
    }


    /**
     * 目标方法返回后执行以下代码
     * pointcut 属性：绑定通知的切入点表达式，优先级高于 value，默认为 ""
     * returning 属性：通知签名中要将返回值绑定到的参数的名称，默认为 ""
     */
    @AfterReturning(pointcut = "start()", returning = "object")
    public void afterReturning(Object object) {
        if (logEntityThreadLocal.get() != null) {
            LogEntity logEntity = logEntityThreadLocal.get();
            if (startTimeThreadLocal.get() != null) {
                Long startTime = startTimeThreadLocal.get();
                // 消耗时间
                logEntity.setConsumeTime(System.currentTimeMillis() - startTime);
            }

            // 响应结果
            logEntity.setResponseResult(JSONObject.toJSONString(object));
            // 操作时间
            logEntity.setOperateTime(new Date());

            if (logger.isInfoEnabled()) {
                logger.info("请求处理结束,参数：[ " + JSON.toJSONString(logEntity) + " ]");
            }

            Boolean persistentFlag = null;

            if (isPersistentThreadLocal.get() != null) {
                persistentFlag = isPersistentThreadLocal.get();
            }

            // 处理日志
            try {
                logHandler.processLog(logEntity, persistentFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 可以加快内存的释放
        startTimeThreadLocal.remove();
        logEntityThreadLocal.remove();
        isPersistentThreadLocal.remove();
    }


    /**
     * 异常通知：目标方法发生异常的时候执行以下代码，此时返回通知不会再触发
     */
    @AfterThrowing(pointcut = "start()", throwing = "throwable")
    public void afterThrowing(Throwable throwable) {
        logger.error("业务处理发生异常", throwable);

        if (logger.isInfoEnabled()) {
            logger.info("业务处理发生异常 " + throwable.getMessage());
        }

        if (logEntityThreadLocal.get() != null) {
            LogEntity logEntity = logEntityThreadLocal.get();
            if (startTimeThreadLocal.get() != null) {
                Long startTime = startTimeThreadLocal.get();
                logEntity.setConsumeTime(System.currentTimeMillis() - startTime);
            }

            // 异常信息
            logEntity.setExceptionMsg(throwable.getMessage());
            logEntity.setOperateTime(new Date());

            if (logger.isInfoEnabled()) {
                logger.info("请求处理异常,参数：[ " + JSON.toJSONString(logEntity) + " ]");
            }

            Boolean persistentFlag = false;

            if (isPersistentThreadLocal.get() != null) {
                persistentFlag = isPersistentThreadLocal.get();
            }

            // 处理日志
            try {
                logHandler.processLog(logEntity, persistentFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        startTimeThreadLocal.remove();
        logEntityThreadLocal.remove();
        isPersistentThreadLocal.remove();
    }


}
