package com.feixue.mbridge.domain.proxy;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zxxiao on 2017/7/13.
 */
public class HttpProxyContent implements Serializable {
    private static final long serialVersionUID = 1519311579782721273L;

    private String url;

    private String method;

    private Map<String, String> headersMap;

    private String contentType;

    private Object body;

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

    public Map<String, String> getHeadersMap() {
        return headersMap;
    }

    public void setHeadersMap(Map<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
