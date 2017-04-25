package com.feixue.mbridge.domain.protocol;

import java.io.Serializable;

public class ProtocolParam implements Serializable {
    private static final long serialVersionUID = -3629406804556936142L;

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

    public ProtocolParam() {
    }

    public ProtocolParam(String paramName, Object paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.required = true;
    }

    public ProtocolParam(String paramName, Object paramValue, boolean required) {
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
        return "ProtocolParam{" +
                "paramName='" + paramName + '\'' +
                ", paramValue=" + paramValue +
                ", required=" + required +
                '}';
    }
}
