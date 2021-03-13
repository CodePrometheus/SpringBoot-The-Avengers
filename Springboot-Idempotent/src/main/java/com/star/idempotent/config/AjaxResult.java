package com.star.idempotent.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zzStar
 * @Date: 03-13-2021 10:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResult implements Serializable {

    private static final long serialVersionUID = 7498483649536881777L;

    private Integer status;

    private String msg;

    private Object data;

    @JsonIgnore
    public boolean isSuccess() {
        return this.status.equals(ResponseCode.SUCCESS.getCode());
    }

    public static AjaxResult success() {
        return new AjaxResult(ResponseCode.SUCCESS.getCode(), null, null);
    }

    public static AjaxResult success(String msg) {
        return new AjaxResult(ResponseCode.SUCCESS.getCode(), msg, null);
    }

    public static AjaxResult success(Object data) {
        return new AjaxResult(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMsg(), data);
    }

    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static AjaxResult error(String msg) {
        return new AjaxResult(ResponseCode.ERROR.getCode(), msg, null);
    }

    public static AjaxResult error(Object data) {
        return new AjaxResult(ResponseCode.ERROR.getCode(), null, data);
    }

    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(ResponseCode.ERROR.getCode(), msg, data);
    }
}
