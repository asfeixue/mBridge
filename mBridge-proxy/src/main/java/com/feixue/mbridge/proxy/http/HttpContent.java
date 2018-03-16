package com.feixue.mbridge.proxy.http;

import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.proxy.Content;

import java.util.List;

public class HttpContent implements Content {

    /**
     * 请求url
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * header 集合
     */
    private List<ProtocolHeader> headerList;

    /**
     * 请求体
     */
    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<ProtocolHeader> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<ProtocolHeader> headerList) {
        this.headerList = headerList;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
