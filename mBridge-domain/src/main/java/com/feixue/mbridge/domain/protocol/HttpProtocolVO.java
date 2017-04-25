package com.feixue.mbridge.domain.protocol;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.domain.tpl.HeaderTplDO;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HttpProtocolVO implements Serializable {
    private static final long serialVersionUID = 7320179040834281921L;

    /**
     * 协议媒体类型
     */
    public interface ProtocolContentType {
        String form = "form";
        String json = "json";
    }

    /*
    协议编号
     */
    private long id;

    /*
    URL地址
     */
    private String urlPath;

    /*
    服务端提供客户端接入的地址详情
     */
    private String host;

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
    private List<ProtocolParam> paramList;

    /*
    路径参数集合
     */
    private List<ProtocolPath> pathList;

    /*
    请求类型
     */
    private String requestType;

    /*
    请求实体
     */
    private Object requestBody;

    /*
    响应实体
     */
    private Object responseBody;

    /*
    请求header
     */
    private List<ProtocolHeader> requestHeader;

    /*
    请求header订阅header模板ID
     */
    private HeaderTplDO requestHeaderSubscribe;


    /*
    响应header
     */
    private List<ProtocolHeader> responseHeader;

    /*
    响应header订阅header模板ID
     */
    private HeaderTplDO responseHeaderSubscribe;

    /*
    所属系统信息
     */
    private SystemDO system;

    /*
    是否存在争议
     */
    private boolean dispute;

    public HttpProtocolVO() {
    }

    public HttpProtocolVO(HttpProtocolDO protocolDO, SystemDO system) {
        this.id = protocolDO.getId();
        this.system = system;
        this.urlPath = protocolDO.getUrlPath();
        this.urlDesc = protocolDO.getUrlDesc();
        this.contentType = protocolDO.getContentType();
        this.paramList = JSON.parseArray(protocolDO.getParamList(), ProtocolParam.class);
        this.pathList = JSON.parseArray(protocolDO.getPathList(), ProtocolPath.class);
        this.requestType = protocolDO.getRequestType();

        if (requestType.equalsIgnoreCase("post")) {
            if (contentType.equalsIgnoreCase(ProtocolContentType.json)) {
                this.requestBody = JSON.parseObject(protocolDO.getRequestBody(), Object.class);
            } else {
                this.requestBody = JSON.parseArray(protocolDO.getRequestBody(), Object.class);
            }
        }

        this.responseBody = JSON.parseObject(protocolDO.getResponseBody(), Object.class);

        List<ProtocolHeader> requestHeaderList = JSON.parseArray(protocolDO.getRequestHeader(), ProtocolHeader.class);
        this.requestHeader = requestHeaderList == null ? Collections.EMPTY_LIST : requestHeaderList;

        List<ProtocolHeader> responseHeaderList = JSON.parseArray(protocolDO.getResponseHeader(), ProtocolHeader.class);
        this.responseHeader = responseHeaderList == null ? Collections.EMPTY_LIST : responseHeaderList;

        this.dispute = protocolDO.isDispute();
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public List<ProtocolParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ProtocolParam> paramList) {
        this.paramList = paramList;
    }

    public List<ProtocolPath> getPathList() {
        return pathList;
    }

    public void setPathList(List<ProtocolPath> pathList) {
        this.pathList = pathList;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
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

    public List<ProtocolHeader> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(List<ProtocolHeader> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public HeaderTplDO getRequestHeaderSubscribe() {
        return requestHeaderSubscribe;
    }

    public void setRequestHeaderSubscribe(HeaderTplDO requestHeaderSubscribe) {
        this.requestHeaderSubscribe = requestHeaderSubscribe;
    }

    public List<ProtocolHeader> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(List<ProtocolHeader> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public HeaderTplDO getResponseHeaderSubscribe() {
        return responseHeaderSubscribe;
    }

    public void setResponseHeaderSubscribe(HeaderTplDO responseHeaderSubscribe) {
        this.responseHeaderSubscribe = responseHeaderSubscribe;
    }

    public SystemDO getSystem() {
        return system;
    }

    public void setSystem(SystemDO system) {
        this.system = system;
    }

    public boolean isDispute() {
        return dispute;
    }

    public void setDispute(boolean dispute) {
        this.dispute = dispute;
    }

    public String getRequestUrl() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(urlPath);
        if (paramList != null && !paramList.isEmpty()) {
            buffer.append("?");
        } else {
            return buffer.toString();
        }
        for(ProtocolParam protocolParam : paramList) {
            buffer.append(protocolParam.getParamName());
            buffer.append("=");
            buffer.append(protocolParam.getParamValue());
            buffer.append("&");
        }
        String requestUrl = buffer.toString();
        if (requestUrl.endsWith("&")) {
            requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
        }
        return requestUrl;
    }

    @Override
    public String toString() {
        return "HttpProtocolVO{" +
                "id=" + id +
                ", urlPath='" + urlPath + '\'' +
                ", host='" + host + '\'' +
                ", urlDesc='" + urlDesc + '\'' +
                ", contentType='" + contentType + '\'' +
                ", paramList=" + paramList +
                ", pathList=" + pathList +
                ", requestType='" + requestType + '\'' +
                ", requestBody=" + requestBody +
                ", responseBody=" + responseBody +
                ", requestHeader=" + requestHeader +
                ", requestHeaderSubscribe=" + requestHeaderSubscribe +
                ", responseHeader=" + responseHeader +
                ", responseHeaderSubscribe=" + responseHeaderSubscribe +
                ", system=" + system +
                ", dispute=" + dispute +
                '}';
    }
}
