package com.feixue.mbridge.domain.request;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/6/1.
 */
public class MockRequestDO implements Serializable {
    private static final long serialVersionUID = 3263219594506315583L;

    private long id;

    /*
    协议编号
     */
    private long protocolId;

    /*
    请求header
     */
    private String requestHeader;

    /*
    请求header订阅header模板ID
     */
    private long requestHeaderSubscribe;

    /*
    协议约定请求header
     */
    private String protocolRequestHeader;

    /*
    协议约定请求header订阅header模板ID
     */
    private long protocolRequestHeaderSubscribe;

    /*
    参数集合
     */
    private String paramList;

    /*
    请求实体
     */
    private String requestBody;

    /*
    系统识别码
     */
    private String systemCode;

    /*
    执行计划
     */
    private String schedulePlan;

    /*
    是否开启计划。0：不是；1：是
     */
    private int isScheduler;

    /*
    保留测试报告数
     */
    private int keepReport;

    /*
    mock描述
     */
    private String mockDesc;

    /*
    支持环境
     */
    private long supportEnv;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更新时间
     */
    private String gmtModify;

    public MockRequestDO() {
    }

    public MockRequestDO(MockRequestVO requestVO) {
        this.id = requestVO.getId();
        this.protocolId = requestVO.getProtocolId();
        this.requestHeader = JSON.toJSONString(requestVO.getRequestHeader());
        this.requestHeaderSubscribe = requestVO.getRequestHeaderSubscribe() == null ? 0 : requestVO.getRequestHeaderSubscribe().getId();
        this.protocolRequestHeader = JSON.toJSONString(requestVO.getProtocolRequestHeader());

        if (requestVO.getProtocolRequestHeaderSubscribe() != null) {
            this.protocolRequestHeaderSubscribe = requestVO.getProtocolRequestHeaderSubscribe().getId();
        }

        this.paramList = JSON.toJSONString(requestVO.getParamList());
        this.requestBody = requestVO.getRequestBody();
        this.systemCode = requestVO.getSystemCode();
        this.schedulePlan = requestVO.getSchedulePlan();
        this.isScheduler = requestVO.getIsScheduler();
        this.keepReport = requestVO.getKeepReport();
        this.mockDesc = requestVO.getMockDesc();
        this.supportEnv = requestVO.getSupportEnv().getId();
        this.gmtCreate = requestVO.getGmtCreate();
        this.gmtModify = requestVO.getGmtModify();
    }

    public MockRequestDO(HttpProtocolDO protocolPO) {
        this.setProtocolId(protocolPO.getId());
        this.setParamList(protocolPO.getParamList());
        this.setRequestBody(protocolPO.getRequestBody());
        this.setRequestHeader(protocolPO.getRequestHeader());
        this.setSystemCode(protocolPO.getSystemCode());
        this.setKeepReport(10); //默认保留10个测试报告
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

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public long getRequestHeaderSubscribe() {
        return requestHeaderSubscribe;
    }

    public void setRequestHeaderSubscribe(long requestHeaderSubscribe) {
        this.requestHeaderSubscribe = requestHeaderSubscribe;
    }

    public String getProtocolRequestHeader() {
        return protocolRequestHeader;
    }

    public void setProtocolRequestHeader(String protocolRequestHeader) {
        this.protocolRequestHeader = protocolRequestHeader;
    }

    public long getProtocolRequestHeaderSubscribe() {
        return protocolRequestHeaderSubscribe;
    }

    public void setProtocolRequestHeaderSubscribe(long protocolRequestHeaderSubscribe) {
        this.protocolRequestHeaderSubscribe = protocolRequestHeaderSubscribe;
    }

    public String getParamList() {
        return paramList;
    }

    public void setParamList(String paramList) {
        this.paramList = paramList;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSchedulePlan() {
        return schedulePlan;
    }

    public void setSchedulePlan(String schedulePlan) {
        this.schedulePlan = schedulePlan;
    }

    public int getIsScheduler() {
        return isScheduler;
    }

    public void setIsScheduler(int isScheduler) {
        this.isScheduler = isScheduler;
    }

    public int getKeepReport() {
        return keepReport;
    }

    public void setKeepReport(int keepReport) {
        this.keepReport = keepReport;
    }

    public String getMockDesc() {
        return mockDesc;
    }

    public void setMockDesc(String mockDesc) {
        this.mockDesc = mockDesc;
    }

    public long getSupportEnv() {
        return supportEnv;
    }

    public void setSupportEnv(long supportEnv) {
        this.supportEnv = supportEnv;
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
        return "MockRequestDO{" +
                "id=" + id +
                ", protocolId=" + protocolId +
                ", requestHeader='" + requestHeader + '\'' +
                ", requestHeaderSubscribe=" + requestHeaderSubscribe +
                ", protocolRequestHeader='" + protocolRequestHeader + '\'' +
                ", protocolRequestHeaderSubscribe=" + protocolRequestHeaderSubscribe +
                ", paramList='" + paramList + '\'' +
                ", requestBody='" + requestBody + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", schedulePlan='" + schedulePlan + '\'' +
                ", isScheduler=" + isScheduler +
                ", keepReport=" + keepReport +
                ", mockDesc='" + mockDesc + '\'' +
                ", supportEnv='" + supportEnv + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
