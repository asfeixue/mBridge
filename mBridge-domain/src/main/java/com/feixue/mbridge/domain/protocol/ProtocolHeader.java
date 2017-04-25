package com.feixue.mbridge.domain.protocol;

import java.io.Serializable;

public class ProtocolHeader implements Serializable {
    private static final long serialVersionUID = -5671918052865254175L;

    /*
    协议编号
     */
    private long protocolId;

    /*
    协议头key
     */
    private String headerKey;

    /*
    协议头value
     */
    private String headerValue;

    public ProtocolHeader() {
    }

    public ProtocolHeader(String headerKey, String headerValue) {
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }

    public ProtocolHeader(long protocolId, String headerKey, String headerValue) {
        this.protocolId = protocolId;
        this.headerKey = headerKey;
        this.headerValue = headerValue;
    }

    public long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(long protocolId) {
        this.protocolId = protocolId;
    }

    public String getHeaderKey() {
        return headerKey;
    }

    public void setHeaderKey(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public String toString() {
        return "ProtocolHeader{" +
                "protocolId=" + protocolId +
                ", headerKey='" + headerKey + '\'' +
                ", headerValue='" + headerValue + '\'' +
                '}';
    }

    /**
     * 头类型  0:协议约定； 1:请求；   2:响应
     */
    public interface HeaderType {
        int protocol = 0;
        int request = 1;
        int response = 2;
    }
}
