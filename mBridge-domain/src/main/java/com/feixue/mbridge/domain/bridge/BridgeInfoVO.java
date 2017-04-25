package com.feixue.mbridge.domain.bridge;

import com.feixue.mbridge.domain.protocol.ProtocolHeader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/6/19.
 */
public class BridgeInfoVO implements Serializable {
    private static final long serialVersionUID = -4277166396058954834L;

    private long id;

    /*
    请求url
     */
    private String url;

    /*
    完整url
     */
    private String completeUrl;

    /*
    http状态
     */
    private int httpStatus;

    /*
    http方法
     */
    private String httpMethod;

    /*
    请求header
     */
    private List<ProtocolHeader> requestHeader;

    /*
    请求body
     */
    private String requestBody;

    /*
    响应header
     */
    private List<ProtocolHeader> responseHeader;

    /*
    响应body
     */
    private String responseBody;

    /*
    系统code
     */
    private String systemCode;

    /*
    错误描述信息
     */
    private String errorInfo;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更新时间
     */
    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompleteUrl() {
        return completeUrl;
    }

    public void setCompleteUrl(String completeUrl) {
        this.completeUrl = completeUrl;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public List<ProtocolHeader> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(List<ProtocolHeader> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public List<ProtocolHeader> getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(List<ProtocolHeader> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "BridgeInfoVO{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", completeUrl='" + completeUrl + '\'' +
                ", httpStatus=" + httpStatus +
                ", httpMethod='" + httpMethod + '\'' +
                ", requestHeader=" + requestHeader +
                ", requestBody='" + requestBody + '\'' +
                ", responseHeader=" + responseHeader +
                ", responseBody='" + responseBody + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
