package com.feixue.mbridge.domain;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/6/13.
 */
public class BusinessWrapper<T> implements Serializable {
    private static final long serialVersionUID = 3900574490592956332L;

    /*
    响应实体
     */
    private T body;

    /*
    描述
     */
    private String msg;

    /*
    错误码
     */
    private String code;

    private boolean success;

    public BusinessWrapper(T body) {
        this.body = body;
        this.success = true;
    }

    public BusinessWrapper(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.success = false;
    }


    public BusinessWrapper(T body, String msg, String code) {
        this.body = body;
        this.msg = msg;
        this.code = code;
        this.success = false;
    }

    public BusinessWrapper(ErrorCode errorCode) {
        this.msg = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.success = false;
    }

    public BusinessWrapper(T body, ErrorCode errorCode) {
        this.body = body;
        this.msg = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.success = false;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BusinessWrapper{" +
                "body=" + body +
                ", msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", success=" + success +
                '}';
    }
}
