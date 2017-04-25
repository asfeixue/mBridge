package com.feixue.mbridge.meta.domain;

import java.io.Serializable;

/**
 * Created by zxxiao on 2017/2/6.
 */
public class HttpProtocolParam implements Serializable {
    private static final long serialVersionUID = -7939687984479183844L;

    /*
    参数名称
     */
    private String paramName;

    /*
    参数值
     */
    private Object paramValue;

    /*
    是否必须
     */
    private boolean required;

    public HttpProtocolParam() {
    }

    public HttpProtocolParam(String paramName, Object paramValue, boolean required) {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.required = required;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "HttpProtocolParam{" +
                "paramName='" + paramName + '\'' +
                ", paramValue=" + paramValue +
                ", required=" + required +
                '}';
    }
}
