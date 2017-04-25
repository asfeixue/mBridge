package com.feixue.mbridge.domain.workflow;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/8/7.
 */
public class LinkNodeDO implements Serializable {
    private static final long serialVersionUID = 1297915224465590411L;

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
    private long protocolId;

    /**
     * 执行环境id
     */
    private long envId;

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

    public LinkNodeDO() {
    }

    public LinkNodeDO(LinkNodeVO nodeVO) {
        this.id = nodeVO.getId();
        this.flowId = nodeVO.getFlowId();
        this.headerScript = nodeVO.getHeaderScript();
        this.protocolId = nodeVO.getProtocolVO().getId();
        this.envId = nodeVO.getSystemEnvDO().getId();
        this.endScript = nodeVO.getEndScript();
        this.gmtCreate = nodeVO.getGmtCreate();
        this.gmtModify = nodeVO.getGmtModify();
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

    public long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(long protocolId) {
        this.protocolId = protocolId;
    }

    public long getEnvId() {
        return envId;
    }

    public void setEnvId(long envId) {
        this.envId = envId;
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
        return "LinkNodeDO{" +
                "id=" + id +
                ", flowId=" + flowId +
                ", headerScript='" + headerScript + '\'' +
                ", protocolId=" + protocolId +
                ", envId=" + envId +
                ", endScript='" + endScript + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
