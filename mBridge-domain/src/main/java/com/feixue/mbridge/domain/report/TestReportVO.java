package com.feixue.mbridge.domain.report;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.domain.protocol.ProtocolParam;
import com.feixue.mbridge.domain.system.SystemDO;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 16/7/2.
 */
public class TestReportVO implements Serializable {
    private static final long serialVersionUID = -9107625206647222148L;

    private long id;

    /*
    对应协议编号
     */
    private long protocolId;

    /*
    系统编码
     */
    private SystemDO systemCode;

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
    private List<ProtocolHeader> requestHeader;

    /*
    请求体媒体类型
     */
    private String requestContentType;

    /*
    请求参数
     */
    private List<ProtocolParam> requestParam;

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
    private List<ProtocolHeader> mockRequestHeader;

    /*
    mock请求参数
     */
    private List<ProtocolParam> mockRequestParam;

    /*
    mock请求body
     */
    private String mockRequestBody;

    /*
    mock响应header
     */
    private List<ProtocolHeader> mockResponseHeader;

    /*
    mock响应body
     */
    private String mockResponseBody;

    /*
    检查报告
     */
    private Map<String, CheckReport> checkReport;

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

    public TestReportVO() {
    }

    public TestReportVO(TestReportDO testReportDO, SystemDO systemCode) {
        this.id = testReportDO.getId();
        this.protocolId = testReportDO.getProtocolId();
        this.systemCode = systemCode;
        this.statusCode = testReportDO.getStatusCode();
        this.requestInfo = testReportDO.getRequestInfo();
        this.requestHeader = testReportDO.getRequestHeader() == null ? Collections.EMPTY_LIST : JSON.parseArray(testReportDO.getRequestHeader(), ProtocolHeader.class);
        this.requestContentType = testReportDO.getRequestContentType();
        this.requestParam = testReportDO.getRequestParam() == null ? Collections.EMPTY_LIST : JSON.parseArray(testReportDO.getRequestParam(), ProtocolParam.class);
        this.requestBody = testReportDO.getRequestBody();
        this.responseHeader = testReportDO.getResponseHeader() == null ? Collections.EMPTY_LIST : JSON.parseArray(testReportDO.getResponseHeader(), ProtocolHeader.class);
        this.responseBody = testReportDO.getResponseBody();

        this.redirectUrl = testReportDO.getRedirectUrl();

        this.mockRequestInfo = testReportDO.getMockRequestInfo();
        this.mockRequestHeader = testReportDO.getMockRequestHeader() == null ? Collections.EMPTY_LIST : JSON.parseArray(testReportDO.getMockRequestHeader(), ProtocolHeader.class);
        this.mockRequestParam = testReportDO.getMockRequestParam() == null ? Collections.EMPTY_LIST : JSON.parseArray(testReportDO.getMockRequestParam(), ProtocolParam.class);
        this.mockRequestBody = testReportDO.getMockRequestBody();
        this.mockResponseHeader = testReportDO.getMockResponseHeader() == null ? Collections.EMPTY_LIST : JSON.parseArray(testReportDO.getMockResponseHeader(), ProtocolHeader.class);
        this.mockResponseBody = testReportDO.getMockResponseBody();
        this.checkReport = testReportDO.getCheckReport() == null ? Collections.EMPTY_MAP : JSON.parseObject(testReportDO.getCheckReport(), Map.class);
        this.testType = testReportDO.getTestType();
        this.testResult = testReportDO.getTestResult();
        this.gmtCreate = testReportDO.getGmtCreate();
        this.gmtModify = testReportDO.getGmtModify();
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

    public SystemDO getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(SystemDO systemCode) {
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

    public List<ProtocolHeader> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(List<ProtocolHeader> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public List<ProtocolParam> getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(List<ProtocolParam> requestParam) {
        this.requestParam = requestParam;
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

    public List<ProtocolHeader> getMockRequestHeader() {
        return mockRequestHeader;
    }

    public void setMockRequestHeader(List<ProtocolHeader> mockRequestHeader) {
        this.mockRequestHeader = mockRequestHeader;
    }

    public List<ProtocolParam> getMockRequestParam() {
        return mockRequestParam;
    }

    public void setMockRequestParam(List<ProtocolParam> mockRequestParam) {
        this.mockRequestParam = mockRequestParam;
    }

    public String getMockRequestBody() {
        return mockRequestBody;
    }

    public void setMockRequestBody(String mockRequestBody) {
        this.mockRequestBody = mockRequestBody;
    }

    public List<ProtocolHeader> getMockResponseHeader() {
        return mockResponseHeader;
    }

    public void setMockResponseHeader(List<ProtocolHeader> mockResponseHeader) {
        this.mockResponseHeader = mockResponseHeader;
    }

    public String getMockResponseBody() {
        return mockResponseBody;
    }

    public void setMockResponseBody(String mockResponseBody) {
        this.mockResponseBody = mockResponseBody;
    }

    public Map<String, CheckReport> getCheckReport() {
        return checkReport;
    }

    public void setCheckReport(Map<String, CheckReport> checkReport) {
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
        return "TestReportVO{" +
                "id=" + id +
                ", protocolId=" + protocolId +
                ", systemCode=" + systemCode +
                ", statusCode=" + statusCode +
                ", requestInfo='" + requestInfo + '\'' +
                ", requestHeader=" + requestHeader +
                ", requestContentType='" + requestContentType + '\'' +
                ", requestParam=" + requestParam +
                ", requestBody='" + requestBody + '\'' +
                ", responseHeader=" + responseHeader +
                ", responseBody='" + responseBody + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                ", mockRequestInfo='" + mockRequestInfo + '\'' +
                ", mockRequestHeader=" + mockRequestHeader +
                ", mockRequestParam=" + mockRequestParam +
                ", mockRequestBody='" + mockRequestBody + '\'' +
                ", mockResponseHeader=" + mockResponseHeader +
                ", mockResponseBody='" + mockResponseBody + '\'' +
                ", checkReport=" + checkReport +
                ", testType='" + testType + '\'' +
                ", testResult=" + testResult +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
