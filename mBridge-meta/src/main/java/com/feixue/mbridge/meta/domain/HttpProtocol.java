package com.feixue.mbridge.meta.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by zxxiao on 2017/2/6.
 */
public class HttpProtocol implements Serializable {

    private static final long serialVersionUID = 7320179040834281921L;

    /*
    索引以及搜索用
     */
    private String queryUrlPath;

    /*
    URL地址
     */
    private String urlPath;

    /*
    URL地址描述
     */
    private String urlDesc;

    /*
    媒体类型
     */
    private String contentType;

    /*
    参数集合
     */
    private List<HttpProtocolParam> paramList;

    /*
    路径参数集合
     */
    private List<HttpProtocolPath> pathList;

    /*
    请求类型
     */
    private Set<String> requestTypeSet;

    /*
    请求实体
     */
    private Object requestBody;

    /*
    响应实体
     */
    private Object responseBody;

    /*
    系统编码
     */
    private String systemCode;

    /*
    是否存在争议
     */
    private boolean dispute;

    public HttpProtocol() {
    }

    public String getQueryUrlPath() {
        return queryUrlPath;
    }

    public void setQueryUrlPath(String queryUrlPath) {
        this.queryUrlPath = queryUrlPath;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getUrlDesc() {
        return urlDesc;
    }

    public void setUrlDesc(String urlDesc) {
        this.urlDesc = urlDesc;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<HttpProtocolParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<HttpProtocolParam> paramList) {
        this.paramList = paramList;
    }

    public List<HttpProtocolPath> getPathList() {
        return pathList;
    }

    public void setPathList(List<HttpProtocolPath> pathList) {
        this.pathList = pathList;
    }

    public Set<String> getRequestTypeSet() {
        return requestTypeSet;
    }

    public void setRequestTypeSet(Set<String> requestTypeSet) {
        this.requestTypeSet = requestTypeSet;
    }

    public void addRequestType(String requestType) {
        this.requestTypeSet.add(requestType);
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public boolean isDispute() {
        return dispute;
    }

    public void setDispute(boolean dispute) {
        this.dispute = dispute;
    }
}
