package com.feixue.mbridge.domain;

import java.io.Serializable;

public class HttpResponse<T> implements Serializable {
    private static final long serialVersionUID = -5518257812747216349L;

    /*
    请求结果
     */
    private boolean success;

    /*
    响应描述
     */
    private String msg;

    /*
    错误码
     */
    private String code;

    private T body;

    public HttpResponse() {
    }

    public HttpResponse(ErrorCode errorCode) {
        this.success = false;
        this.msg = errorCode.getMsg();
        this.code = errorCode.getCode();
    }

    public HttpResponse(String code, String msg) {
        this.success = false;
        this.code = code;
        this.msg = msg;
    }

    public HttpResponse(T body) {
        this.body = body;
        this.success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", body=" + body +
                '}';
    }
}
