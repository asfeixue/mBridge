package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.ProtocolDao;
import com.feixue.mbridge.dao.WorkflowDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.domain.report.TestReportVO;
import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.domain.workflow.LinkNodeDO;
import com.feixue.mbridge.domain.workflow.LinkNodeVO;
import com.feixue.mbridge.domain.workflow.WorkflowDO;
import com.feixue.mbridge.service.SystemService;
import com.feixue.mbridge.service.TestReportService;
import com.feixue.mbridge.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zxxiao on 16/8/7.
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);

    @Resource
    private WorkflowDao workflowDao;

    @Resource
    private ProtocolDao protocolDao;

    @Resource
    private SystemService systemService;

    @Resource
    private TestReportService testReportService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public WorkflowDO queryFlow(long flowId) {
        WorkflowDO workflowDO = workflowDao.queryFlow(flowId);
        if (workflowDO == null) {
            return null;
        }

        setFlowNode(workflowDO);

        return workflowDO;
    }

    @Override
    public TablePageVO<List<WorkflowDO>> queryFlowList(long start, int length) {
        long size = workflowDao.queryFlowSize();
        List<WorkflowDO> list = workflowDao.queryFlowPage(start, length);
        if (list == null || list.isEmpty()) {
            return new TablePageVO<List<WorkflowDO>>(Collections.EMPTY_LIST, size);
        }

        for(WorkflowDO flowDO : list) {
            setFlowNode(flowDO);
        }

        return new TablePageVO<>(list, size);
    }

    @Override
    public BusinessWrapper<Boolean> updateFlow(final WorkflowDO workflowDO) {
        boolean result = transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    if (workflowDO.getId() == 0) {
                        int news = workflowDao.addWorkflow(workflowDO);
                        if (news != 0 && workflowDO.getNodeVOList() != null) {
                            for(LinkNodeVO nodeVO : workflowDO.getNodeVOList()) {
                                nodeVO.setFlowId(workflowDO.getId());

                                LinkNodeDO nodeDO = new LinkNodeDO(nodeVO);

                                workflowDao.addFlowNode(nodeDO);
                            }
                        } else {
                            //新增行数为0，或者无节点，不进行下一步处理
                        }
                    } else {
                        int updates = workflowDao.updateWorkflow(workflowDO);
                        if (updates != 0 && workflowDO.getNodeVOList() != null) {
                            workflowDao.delWorkflowNodeByFlowId(workflowDO.getId());

                            for(LinkNodeVO nodeVO : workflowDO.getNodeVOList()) {
                                nodeVO.setFlowId(workflowDO.getId());

                                LinkNodeDO nodeDO = new LinkNodeDO(nodeVO);

                                workflowDao.addFlowNode(nodeDO);
                            }
                        } else {
                            //更新行数为0，或者无节点，不进行下一步处理
                        }
                    }
                    return true;
                } catch (Exception e) {
                    logger.error("新增 or 更新：" + workflowDO.toString() + " 失败", e);
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
        if (result) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.serviceFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> deleteFlow(final long flowId) {
        boolean result = transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    workflowDao.delWorkflowById(flowId);
                    workflowDao.delWorkflowNodeByFlowId(flowId);
                    return true;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
        if (result) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(ErrorCode.serviceFailure);
        }
    }

    /**
     * 设置任务流的节点数据
     * @param workflowDO
     */
    private void setFlowNode(WorkflowDO workflowDO) {
        List<LinkNodeDO> list = workflowDao.queryFlowNodes(workflowDO.getId());
        if (list == null || list.isEmpty()) {
            workflowDO.setNodeVOList(Collections.EMPTY_LIST);
        } else {
            List<LinkNodeVO> voList = new ArrayList<>();
            for(LinkNodeDO nodeDO : list) {
                LinkNodeVO nodeVO;

                HttpProtocolDO protocolDO = protocolDao.getProtocol(nodeDO.getProtocolId());
                if (protocolDO != null) {
                    SystemDO systemDO = systemService.querySystem(protocolDO.getSystemCode());
                    if (systemDO != null) {
                        HttpProtocolVO protocolVO = new HttpProtocolVO(protocolDO, systemDO);

                        SystemEnvDO systemEnvDO = systemService.getEnvById(nodeDO.getEnvId());

                        TestReportVO serverTestReportVO = testReportService.getServerLastReport(protocolVO.getId());

                        nodeVO = new LinkNodeVO(nodeDO, protocolVO, serverTestReportVO, systemEnvDO);
                    } else {
                        nodeVO = new LinkNodeVO(nodeDO);
                    }
                } else {
                    nodeVO = new LinkNodeVO(nodeDO);
                }

                voList.add(nodeVO);
            }
            workflowDO.setNodeVOList(voList);
        }
    }
}
