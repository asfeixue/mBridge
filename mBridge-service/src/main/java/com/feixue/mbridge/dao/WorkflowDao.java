package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.workflow.LinkNodeDO;
import com.feixue.mbridge.domain.workflow.WorkflowDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/8/7.
 */
@Component
public interface WorkflowDao {

    /**
     * 查询指定链路id的链路信息
     * @param id
     * @return
     */
    WorkflowDO queryFlow(@Param("id") long id);

    /**
     * 查询任务流总记录数
     * @return
     */
    long queryFlowSize();

    /**
     * 查询分页任务流记录
     * @param start
     * @param length
     * @return
     */
    List<WorkflowDO> queryFlowPage(@Param("start") long start, @Param("length") int length);

    /**
     * 查询指定任务流的所有节点信息
     * @param flowId
     * @return
     */
    List<LinkNodeDO> queryFlowNodes(@Param("flowId") long flowId);

    /**
     * 新增任务流信息
     * @param workflowDO
     * @return
     */
    int addWorkflow(WorkflowDO workflowDO);

    /**
     * 更新任务流信息
     * @param workflowDO
     * @return
     */
    int updateWorkflow(WorkflowDO workflowDO);

    /**
     * 新增任务节点信息
     * @param linkNodeDO
     * @return
     */
    int addFlowNode(LinkNodeDO linkNodeDO);

    /**
     * 更新任务节点信息
     * @param linkNodeDO
     * @return
     */
    int updateFlowNode(LinkNodeDO linkNodeDO);

    /**
     * 删除指定的任务流
     * @param id
     * @return
     */
    int delWorkflowById(@Param("id") long id);

    /**
     * 删除指定任务流的节点
     * @param flowId
     * @return
     */
    int delWorkflowNodeByFlowId(@Param("flowId") long flowId);
}
