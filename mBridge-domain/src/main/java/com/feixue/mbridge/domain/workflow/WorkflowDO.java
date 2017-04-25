package com.feixue.mbridge.domain.workflow;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/8/14.
 */
public class WorkflowDO implements Serializable {
    private static final long serialVersionUID = -3646996282597367975L;

    private long id;

    /**
     * 流程介绍
     */
    private String flowDesc;

    /**
     * 流程节点集合
     */
    private List<LinkNodeVO> nodeVOList;

    /**
     * 创建时间
     */
    private String gmtCreate;

    /**
     * 更新时间
     */
    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlowDesc() {
        return flowDesc;
    }

    public void setFlowDesc(String flowDesc) {
        this.flowDesc = flowDesc;
    }

    public List<LinkNodeVO> getNodeVOList() {
        return nodeVOList;
    }

    public void setNodeVOList(List<LinkNodeVO> nodeVOList) {
        this.nodeVOList = nodeVOList;
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
        return "WorkflowDO{" +
                "id=" + id +
                ", flowDesc='" + flowDesc + '\'' +
                ", nodeVOList=" + nodeVOList +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
