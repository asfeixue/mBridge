package com.feixue.mbridge.domain.request;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.domain.protocol.ProtocolParam;
import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.tpl.HeaderTplVO;

import java.io.Serializable;
import java.util.List;

public class MockRequestVO implements Serializable {
    private static final long serialVersionUID = 7134986881666260450L;

    private long id;

    /*
    协议编号
     */
    private long protocolId;

    /*
    请求header
     */
    private List<ProtocolHeader> requestHeader;

    /*
    请求header订阅header模板ID
     */
    private HeaderTplVO requestHeaderSubscribe;

    /*
    协议约定请求header
     */
    private List<ProtocolHeader> protocolRequestHeader;

    /*
    协议约定请求header订阅header模板ID
     */
    private HeaderTplVO protocolRequestHeaderSubscribe;

    /*
    参数集合
     */
    private List<ProtocolParam> paramList;

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
    支持环境列表
     */
    private SystemEnvDO supportEnv;

    /*
    mock描述
     */
    private String mockDesc;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更新时间
     */
    private String gmtModify;

    public MockRequestVO() {
    }

    public MockRequestVO(MockRequestDO requestDO) {
        this.id = requestDO.getId();
        this.protocolId = requestDO.getProtocolId();
        if (requestDO.getRequestHeaderSubscribe() == 0) {
            this.requestHeader = JSON.parseArray(requestDO.getRequestHeader(), ProtocolHeader.class);
        }

        if (requestDO.getProtocolRequestHeaderSubscribe() == 0) {
            this.protocolRequestHeader = JSON.parseArray(requestDO.getProtocolRequestHeader(), ProtocolHeader.class);
        }

        this.paramList = JSON.parseArray(requestDO.getParamList(), ProtocolParam.class);
        this.requestBody = requestDO.getRequestBody();
        this.systemCode = requestDO.getSystemCode();
        this.schedulePlan = requestDO.getSchedulePlan();
        this.isScheduler = requestDO.getIsScheduler();
        this.keepReport = requestDO.getKeepReport();

        this.mockDesc = requestDO.getMockDesc();
        this.gmtCreate = requestDO.getGmtCreate();
        this.gmtModify = requestDO.getGmtModify();
    }

    public MockRequestVO(HttpProtocolDO protocolDO) {
        this.setProtocolId(protocolDO.getId());
        this.setParamList(JSON.parseArray(protocolDO.getParamList(), ProtocolParam.class));
        this.setRequestBody(protocolDO.getRequestBody());
        this.setRequestHeader(JSON.parseArray(protocolDO.getRequestHeader(), ProtocolHeader.class));
        this.setSystemCode(protocolDO.getSystemCode());
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

    public List<ProtocolHeader> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(List<ProtocolHeader> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public HeaderTplVO getRequestHeaderSubscribe() {
        return requestHeaderSubscribe;
    }

    public void setRequestHeaderSubscribe(HeaderTplVO requestHeaderSubscribe) {
        this.requestHeaderSubscribe = requestHeaderSubscribe;
    }

    public List<ProtocolHeader> getProtocolRequestHeader() {
        return protocolRequestHeader;
    }

    public void setProtocolRequestHeader(List<ProtocolHeader> protocolRequestHeader) {
        this.protocolRequestHeader = protocolRequestHeader;
    }

    public HeaderTplVO getProtocolRequestHeaderSubscribe() {
        return protocolRequestHeaderSubscribe;
    }

    public void setProtocolRequestHeaderSubscribe(HeaderTplVO protocolRequestHeaderSubscribe) {
        this.protocolRequestHeaderSubscribe = protocolRequestHeaderSubscribe;
    }

    public List<ProtocolParam> getParamList() {
        return paramList;
    }

    public void setParamList(List<ProtocolParam> paramList) {
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

    public SystemEnvDO getSupportEnv() {
        return supportEnv;
    }

    public void setSupportEnv(SystemEnvDO supportEnv) {
        this.supportEnv = supportEnv;
    }

    public String getMockDesc() {
        return mockDesc;
    }

    public void setMockDesc(String mockDesc) {
        this.mockDesc = mockDesc;
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
        return "MockRequestVO{" +
                "id=" + id +
                ", protocolId=" + protocolId +
                ", requestHeader=" + requestHeader +
                ", requestHeaderSubscribe=" + requestHeaderSubscribe +
                ", protocolRequestHeader=" + protocolRequestHeader +
                ", protocolRequestHeaderSubscribe=" + protocolRequestHeaderSubscribe +
                ", paramList=" + paramList +
                ", requestBody='" + requestBody + '\'' +
                ", systemCode='" + systemCode + '\'' +
                ", schedulePlan='" + schedulePlan + '\'' +
                ", isScheduler=" + isScheduler +
                ", keepReport=" + keepReport +
                ", supportEnv='" + supportEnv + '\'' +
                ", mockDesc='" + mockDesc + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
