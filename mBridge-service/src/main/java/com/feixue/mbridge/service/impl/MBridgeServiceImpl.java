package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.ProtocolDao;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.domain.request.MockRequestDO;
import com.feixue.mbridge.domain.response.MockResponseDO;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.domain.tpl.HeaderTplDO;
import com.feixue.mbridge.meta.domain.HttpProtocol;
import com.feixue.mbridge.service.BodyService;
import com.feixue.mbridge.service.HeaderTplService;
import com.feixue.mbridge.service.MBridgeService;
import com.feixue.mbridge.service.SystemService;
import com.feixue.mbridge.util.IPUtil;
import com.feixue.mbridge.util.job.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("mBridgeService")
public class MBridgeServiceImpl implements MBridgeService {

    private static final Logger logger = LoggerFactory.getLogger(MBridgeServiceImpl.class);

    @Autowired
    private ProtocolDao protocolDao;

    @Resource
    private BodyService bodyService;

    @Resource
    private SystemService systemService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private SchedulerManager schedulerManager;

    @Resource
    private HeaderTplService headerTplService;

    @Override
    public void collect(List<HttpProtocol> protocols) {
        for(HttpProtocol protocol : protocols) {
            //保存基础协议数据
            HttpProtocolDO protocolDO = new HttpProtocolDO(protocol);
            if (protocolDao.addProtocol(protocolDO) != 0) {
                //保存mock协议数据
                MockRequestDO mockRequestDO = new MockRequestDO(protocolDO);
                bodyService.addMockRequest(mockRequestDO);

                if (protocolDO.getResponseBody() != null) {
                    MockResponseDO mockResponseDO = new MockResponseDO(protocolDO);
                    bodyService.addMockResponse(mockResponseDO);
                }
            }
        }
    }

    @Override
    public TablePageVO<List<HttpProtocolVO>> getProtocols(String url, String systemCode, int page, int length) {
        List<HttpProtocolDO> protocolDOList = protocolDao.getProtocols(url, systemCode, page, length);
        long size = protocolDao.getProtocolSize(url, systemCode);

        List<HttpProtocolVO> protocolVOList = new ArrayList<>();

        for (HttpProtocolDO protocolDO : protocolDOList) {
            HttpProtocolVO protocolVO = buildProtocolDOToVO(protocolDO);

            protocolVOList.add(protocolVO);
        }

        TablePageVO<List<HttpProtocolVO>> pageVO = new TablePageVO<>(protocolVOList, size);

        return pageVO;
    }

    @Override
    public HttpProtocolVO getProtocolDetail(long id) {
        HttpProtocolDO protocolDO = protocolDao.getProtocol(id);

        return buildProtocolDOToVO(protocolDO);
    }

    @Override
    public boolean delProtocol(final long protocolId) {
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    /**
                     * 1.注销定时任务
                     * 2.删除测试报告
                     * 3.删除mock报文
                     * 4.删除协议内容
                     */
                    for(MockRequestDO mockRequestDO : bodyService.getRequestMockByProtocolId(protocolId)) {
                        schedulerManager.unregisterJob(mockRequestDO.getProtocolId() + ":" + mockRequestDO.getId());
                    }
                    protocolDao.delProtocolTestReport(protocolId);

                    bodyService.delRequestMockByProtocolId(protocolId);
                    protocolDao.delProtocol(protocolId);

                    return true;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    status.setRollbackOnly();
                    return false;
                }
            }
        });
    }

    @Override
    public boolean saveProtocol(HttpProtocolVO protocolVO) {
        HttpProtocolDO protocolDO = new HttpProtocolDO(protocolVO);

        try {
            if (protocolDO.getId() == 0) {
                protocolDao.addProtocol(protocolDO);
            } else {
                protocolDao.updateProtocol(protocolDO);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 协议对象类型转换
     * @param protocolDO
     * @return
     */
    private HttpProtocolVO buildProtocolDOToVO(HttpProtocolDO protocolDO) {
        if (protocolDO == null) {
            return null;
        } else {
            HeaderTplDO requestTpl = headerTplService.getTplById(protocolDO.getRequestHeaderSubscribe());
            HeaderTplDO responseTpl = headerTplService.getTplById(protocolDO.getResponseHeaderSubscribe());
            SystemDO systemDO = systemService.querySystem(protocolDO.getSystemCode());

            HttpProtocolVO protocolVO = new HttpProtocolVO(protocolDO, systemDO);
            protocolVO.setRequestHeaderSubscribe(requestTpl);
            protocolVO.setResponseHeaderSubscribe(responseTpl);

            String host = "http://" + IPUtil.getLocalHostIP() + ":8081";
            protocolVO.setHost(host);

            return protocolVO;
        }
    }
}
