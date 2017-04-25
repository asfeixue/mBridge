package com.feixue.mbridge.domain.workflow;

import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.domain.report.TestReportVO;
import com.feixue.mbridge.domain.system.SystemEnvDO;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/8/15.
 */
public class LinkNodeVO implements Serializable {
    private static final long serialVersionUID = 4055478153792782906L;

    /**
     * 节点id
     */
    private long id;

    /**
     * 任务流id
     */
    private long flowId;

    /**
     * 入口脚本
     */
    private String headerScript;

    /**
     * 节点协议
     */
    private HttpProtocolVO protocolVO;

    /**
     * 协议服务端测试报告
     */
    private TestReportVO serverTestReportVO;

    /**
     * 协议执行环境
     */
    private SystemEnvDO systemEnvDO;

    /**
     * 出口脚本
     */
    private String endScript;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 更新时间
     */
    private String gmtModify;

    public LinkNodeVO() {
    }

    public LinkNodeVO(LinkNodeDO nodeDO) {
        this.id = nodeDO.getId();
        this.flowId = nodeDO.getFlowId();
        this.headerScript = nodeDO.getHeaderScript();
        this.endScript = nodeDO.getEndScript();
        this.gmtCreate = nodeDO.getGmtCreate();
        this.gmtModify = nodeDO.getGmtModify();
    }

    public LinkNodeVO(LinkNodeDO nodeDO, HttpProtocolVO protocolVO, TestReportVO serverTestReportVO, SystemEnvDO systemEnvDO) {
        this.id = nodeDO.getId();
        this.flowId = nodeDO.getFlowId();
        this.headerScript = nodeDO.getHeaderScript();
        this.protocolVO = protocolVO;
        this.serverTestReportVO = serverTestReportVO;
        this.systemEnvDO = systemEnvDO;
        this.endScript = nodeDO.getEndScript();
        this.gmtCreate = nodeDO.getGmtCreate();
        this.gmtModify = nodeDO.getGmtModify();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFlowId() {
        return flowId;
    }

    public void setFlowId(long flowId) {
        this.flowId = flowId;
    }

    public String getHeaderScript() {
        return headerScript;
    }

    public void setHeaderScript(String headerScript) {
        this.headerScript = headerScript;
    }

    public HttpProtocolVO getProtocolVO() {
        return protocolVO;
    }

    public void setProtocolVO(HttpProtocolVO protocolVO) {
        this.protocolVO = protocolVO;
    }

    public TestReportVO getServerTestReportVO() {
        return serverTestReportVO;
    }

    public void setServerTestReportVO(TestReportVO serverTestReportVO) {
        this.serverTestReportVO = serverTestReportVO;
    }

    public SystemEnvDO getSystemEnvDO() {
        return systemEnvDO;
    }

    public void setSystemEnvDO(SystemEnvDO systemEnvDO) {
        this.systemEnvDO = systemEnvDO;
    }

    public String getEndScript() {
        return endScript;
    }

    public void setEndScript(String endScript) {
        this.endScript = endScript;
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
        return "LinkNodeVO{" +
                "id=" + id +
                ", flowId=" + flowId +
                ", headerScript='" + headerScript + '\'' +
                ", protocolVO=" + protocolVO +
                ", serverTestReportVO=" + serverTestReportVO +
                ", systemEnvDO=" + systemEnvDO +
                ", endScript='" + endScript + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
