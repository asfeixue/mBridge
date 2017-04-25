package com.feixue.mbridge.domain.report;

import java.io.Serializable;

public class TestReportDO implements Serializable {
    private static final long serialVersionUID = 3631207095343780128L;

    private long id;

    /*
    对应协议编号
     */
    private long protocolId;

    /*
    系统编码
     */
    private String systemCode;

    /*
    HTTP STATUS
     */
    private int statusCode;

    /*
    请求信息
     */
    private String requestInfo;

    /*
    请求header
     */
    private String requestHeader;

    /*
    请求体媒体类型
     */
    private String requestContentType;

    /*
    请求参数
     */
    private String requestParam;

    /*
    请求body
     */
    private String requestBody;

    /*
    响应header
     */
    private String responseHeader;

    /*
    响应body
     */
    private String responseBody;

    /*
    重定向url
     */
    private String redirectUrl;

    /*
    mock请求信息
     */
    private String mockRequestInfo;

    /*
    mock请求header
     */
    private String mockRequestHeader;

    /*
    mock请求参数
     */
    private String mockRequestParam;

    /*
    mock请求body
     */
    private String mockRequestBody;

    /*
    mock响应header
     */
    private String mockResponseHeader;

    /*
    mock响应body
     */
    private String mockResponseBody;

    /*
    检查报告
     */
    private String checkReport;

    /*
    测试类型，client：客户端测试；server：服务端测试
     */
    private String testType;

    /*
    测试结果    0:成功；1：失败；2：警告
     */
    private int testResult;

    /*
    记录创建时间
     */
    private String gmtCreate;

    /*
    记录更新时间
     */
    private String gmtModify;

    public TestReportDO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(long protocolId) {
        this.protocolId = protocolId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(String requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getMockRequestInfo() {
        return mockRequestInfo;
    }

    public void setMockRequestInfo(String mockRequestInfo) {
        this.mockRequestInfo = mockRequestInfo;
    }

    public String getMockRequestHeader() {
        return mockRequestHeader;
    }

    public void setMockRequestHeader(String mockRequestHeader) {
        this.mockRequestHeader = mockRequestHeader;
    }

    public String getMockRequestParam() {
        return mockRequestParam;
    }

    public void setMockRequestParam(String mockRequestParam) {
        this.mockRequestParam = mockRequestParam;
    }

    public String getMockRequestBody() {
        return mockRequestBody;
    }

    public void setMockRequestBody(String mockRequestBody) {
        this.mockRequestBody = mockRequestBody;
    }

    public String getMockResponseHeader() {
        return mockResponseHeader;
    }

    public void setMockResponseHeader(String mockResponseHeader) {
        this.mockResponseHeader = mockResponseHeader;
    }

    public String getMockResponseBody() {
        return mockResponseBody;
    }

    public void setMockResponseBody(String mockResponseBody) {
        this.mockResponseBody = mockResponseBody;
    }

    public String getCheckReport() {
        return checkReport;
    }

    public void setCheckReport(String checkReport) {
        this.checkReport = checkReport;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public int getTestResult() {
        return testResult;
    }

    public void setTestResult(int testResult) {
        this.testResult = testResult;
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
        return "TestReportDO{" +
                "id=" + id +
                ", protocolId=" + protocolId +
                ", systemCode='" + systemCode + '\'' +
                ", statusCode=" + statusCode +
                ", requestInfo='" + requestInfo + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", requestContentType='" + requestContentType + '\'' +
                ", requestParam='" + requestParam + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", responseHeader='" + responseHeader + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", mockRequestInfo='" + mockRequestInfo + '\'' +
                ", mockRequestHeader='" + mockRequestHeader + '\'' +
                ", mockRequestParam='" + mockRequestParam + '\'' +
                ", mockRequestBody='" + mockRequestBody + '\'' +
                ", mockResponseHeader='" + mockResponseHeader + '\'' +
                ", mockResponseBody='" + mockResponseBody + '\'' +
                ", checkReport='" + checkReport + '\'' +
                ", testType='" + testType + '\'' +
                ", testResult=" + testResult +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    /**
     * 测试类型，client：客户端测试；server：服务端测试
     */
    public enum TestType {
        client("client"),
        server("server");
        private String type;

        TestType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 测试结果    0:成功；1：失败；2：警告
     */
    public enum TestResult {
        success(0),
        failure(1),
        warning(2);

        private int code;

        TestResult(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}
