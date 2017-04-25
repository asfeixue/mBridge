package com.feixue.mbridge.domain.protocol;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.meta.domain.HttpProtocol;

import java.io.Serializable;

public class HttpProtocolDO implements Serializable {
    private static final long serialVersionUID = -8636619458424337480L;

    /*
    协议编号
     */
    private long id;

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
    private String paramList;

    /*
    路径参数集合
     */
    private String pathList;

    /*
    请求类型
     */
    private String requestType;

    /*
    请求实体
     */
    private String requestBody;

    /*
    响应实体
     */
    private String responseBody;

    /*
    请求header
     */
    private String requestHeader;

    /*
    请求header订阅header模板ID
     */
    private long requestHeaderSubscribe;

    /*
    响应header
     */
    private String responseHeader;

    /*
    响应header订阅header模板ID
     */
    private long responseHeaderSubscribe;

    /*
    系统识别码
     */
    private String systemCode;

    /*
    是否存在争议
     */
    private boolean dispute;

    public HttpProtocolDO() {
    }

    public HttpProtocolDO(HttpProtocolVO protocol) {
        this.id = protocol.getId();
        this.urlPath = protocol.getUrlPath();
        this.urlDesc = protocol.getUrlDesc();
        this.contentType = protocol.getContentType();
        this.paramList = JSON.toJSONString(protocol.getParamList());
        this.pathList = JSON.toJSONString(protocol.getPathList());
        this.requestType = protocol.getRequestType();
        this.requestBody = protocol.getRequestBody() != null ? JSON.toJSONString(protocol.getRequestBody()) : null;
        this.responseBody = protocol.getResponseBody() != null ? JSON.toJSONString(protocol.getResponseBody()) : null;
        this.requestHeader = JSON.toJSONString(protocol.getRequestHeader());
        this.requestHeaderSubscribe = protocol.getRequestHeaderSubscribe() == null ? 0l : protocol.getRequestHeaderSubscribe().getId();
        this.responseHeader = JSON.toJSONString(protocol.getResponseHeader());
        this.responseHeaderSubscribe = protocol.getResponseHeaderSubscribe() == null ? 0l : protocol.getResponseHeaderSubscribe().getId();
        this.systemCode = protocol.getSystem().getSystemCode();
        this.dispute = protocol.isDispute();
    }

    public HttpProtocolDO(HttpProtocol protocol) {
        this.urlPath = protocol.getUrlPath();
        this.urlDesc = protocol.getUrlDesc();
        this.contentType = protocol.getContentType();
        this.paramList = JSON.toJSONString(protocol.getParamList());
        this.pathList = JSON.toJSONString(protocol.getPathList());
        this.requestType = JSON.toJSONString(protocol.getRequestTypeSet());
        this.requestBody = protocol.getRequestBody() != null ? JSON.toJSONString(protocol.getRequestBody()) : null;
        this.responseBody = protocol.getResponseBody() != null ? JSON.toJSONString(protocol.getResponseBody()) : null;
        this.systemCode = protocol.getSystemCode();
        this.dispute = protocol.isDispute();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getParamList() {
        return paramList;
    }

    public void setParamList(String paramList) {
        this.paramList = paramList;
    }

    public String getPathList() {
        return pathList;
    }

    public void setPathList(String pathList) {
        this.pathList = pathList;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public long getRequestHeaderSubscribe() {
        return requestHeaderSubscribe;
    }

    public void setRequestHeaderSubscribe(int requestHeaderSubscribe) {
        this.requestHeaderSubscribe = requestHeaderSubscribe;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public long getResponseHeaderSubscribe() {
        return responseHeaderSubscribe;
    }

    public void setResponseHeaderSubscribe(int responseHeaderSubscribe) {
        this.responseHeaderSubscribe = responseHeaderSubscribe;
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

    @Override
    public String toString() {
        return "HttpProtocolDO{" +
                "id=" + id +
                ", urlPath='" + urlPath + '\'' +
                ", urlDesc='" + urlDesc + '\'' +
                ", contentType='" + contentType + '\'' +
                ", paramList='" + paramList + '\'' +
                ", pathList='" + pathList + '\'' +
                ", requestType='" + requestType + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", requestHeaderSubscribe=" + requestHeaderSubscribe +
                ", responseHeader='" + responseHeader + '\'' +
                ", responseHeaderSubscribe=" + responseHeaderSubscribe +
                ", systemCode='" + systemCode + '\'' +
                ", dispute=" + dispute +
                '}';
    }
}
