package com.star.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zzStar
 * @Date: 04-08-2021 15:15
 */
@Data
public class LogEntity implements Serializable {

    private String browserInfo;

    private String requestURL;

    private String httpMethod;

    private String requestIP;

    private String requestParam;

    private Object[] requestParams;

    private String operateClassMethod;

    private long consumeTime;

    private String responseResult;

    private String operateDesc;

    private String exceptionMsg;

    private Date operateTime;

    @Override
    public String toString() {
        return String.format("LogEntity[browserInfo='%s',requestURL='%s',httpMethod='%s'," +
                        "requestIP='%s',requestParams='%s',operateClassMethod='%s',consumeTime=%d," +
                        "responseResult='%s',operateDesc='%s',exceptionMsg='%s',operateTime=%d",
                browserInfo, requestURL, httpMethod, requestIP, requestParams, operateClassMethod, consumeTime,
                responseResult, operateDesc, exceptionMsg, operateTime);
    }
}
