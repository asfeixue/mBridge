package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.workflow.WorkflowDO;

import java.util.List;

/**
 * 链路服务
 * Created by zxxiao on 16/8/7.
 */
public interface WorkflowService {

    /**
     * 查询指定id的链路信息
     * @param flowId
     * @return
     */
    WorkflowDO queryFlow(long flowId);

    /**
     * 查询分页任务流
     * @param start
     * @param length
     * @return
     */
    TablePageVO<List<WorkflowDO>> queryFlowList(long start, int length);

    /**
     * 更新链路
     * @param workflowDO
     * @return
     */
    BusinessWrapper<Boolean> updateFlow(WorkflowDO workflowDO);

    /**
     * 删除链路
     * @param flowId
     * @return
     */
    BusinessWrapper<Boolean> deleteFlow(long flowId);
}
