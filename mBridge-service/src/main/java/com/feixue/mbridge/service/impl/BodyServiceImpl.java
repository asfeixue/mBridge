package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.BodyDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.request.MockRequestDO;
import com.feixue.mbridge.domain.request.MockRequestVO;
import com.feixue.mbridge.domain.response.MockResponseDO;
import com.feixue.mbridge.domain.response.MockResponseVO;
import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.tpl.HeaderTplDO;
import com.feixue.mbridge.domain.tpl.HeaderTplVO;
import com.feixue.mbridge.service.BodyService;
import com.feixue.mbridge.service.HeaderTplService;
import com.feixue.mbridge.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 16/4/26.
 */
@Service("bodyService")
public class BodyServiceImpl implements BodyService {

    private static final Logger logger = LoggerFactory.getLogger(BodyServiceImpl.class);

    @Resource
    private BodyDao bodyDao;

    @Resource
    private HeaderTplService headerTplService;

    @Resource
    private SystemService systemService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public BusinessWrapper<MockResponseVO> getProtocolNowResponse(long protocolId) {
        MockResponseDO responseDO = bodyDao.getProtocolNowResponse(protocolId);
        if (responseDO == null) {
            return new BusinessWrapper<>(ErrorCode.protocolNowResponseNotExist);
        }
        MockResponseVO responseVO = new MockResponseVO(responseDO);
        HeaderTplDO headerTplDO = headerTplService.getTplById(responseDO.getHeaderSubscribe());
        if (headerTplDO != null) {
            HeaderTplVO headerTplVO = new HeaderTplVO(headerTplDO);
            responseVO.setHeaderSubscribe(headerTplVO);
        }

        return new BusinessWrapper<>(responseVO);
    }

    @Override
    public boolean addMockResponse(MockResponseVO mockResponseVO) {
        MockResponseDO responseDO = new MockResponseDO(mockResponseVO);
        try {
            if (mockResponseVO.getId() == 0) {
                bodyDao.addMockResponse(responseDO);
            } else {
                bodyDao.updateMockResponseById(responseDO);
            }

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return false;
        }
    }

    @Override
    public boolean addMockResponse(MockResponseDO mockResponseDO) {
        try {
            if (mockResponseDO.getId() == 0) {
                bodyDao.addMockResponse(mockResponseDO);
            } else {
                bodyDao.updateMockResponseById(mockResponseDO);
            }

            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return false;
        }
    }

    @Override
    public boolean delMockResponse(long id) {
        return bodyDao.delMockResponseBody(id);
    }

    @Override
    public boolean setMockResponseNow(final long id, final long protocolId) {
        return transactionTemplate.execute(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doInTransaction(TransactionStatus status) {
                try {
                    bodyDao.updateProtocolMockResponseIsNotNow(protocolId);
                    bodyDao.updateMockResponseNow(MockResponseDO.IsNow.now.getStatus(), id);
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
    public boolean setMockResponseNotNow(long id, long protocolId) {
        return bodyDao.updateMockResponseNow(MockResponseDO.IsNow.notNow.getStatus(), id);
    }

    @Override
    public TablePageVO<List<MockResponseVO>> getMockResponsePage(long protocolId, int pageStart, int pageLength) {
        long size = bodyDao.getMockResponseSize(protocolId);
        List<MockResponseDO> list = bodyDao.getMockResponsePage(protocolId, pageStart * pageLength, pageLength);

        List<MockResponseVO> voList = new ArrayList<>();
        for(MockResponseDO responseDO : list) {
            MockResponseVO responseVO = new MockResponseVO(responseDO);
            HeaderTplDO headerTplDO = headerTplService.getTplById(responseDO.getHeaderSubscribe());
            if (headerTplDO != null) {
                HeaderTplVO headerTplVO = new HeaderTplVO(headerTplDO);
                responseVO.setHeaderSubscribe(headerTplVO);
                responseVO.setHeader(headerTplVO.getTplValue());
            }

            voList.add(responseVO);
        }

        TablePageVO<List<MockResponseVO>> pageVO = new TablePageVO<>(voList, size);

        return pageVO;
    }

    @Override
    public boolean addMockRequest(MockRequestVO mockRequestVO) {
        MockRequestDO mockRequestDO = new MockRequestDO(mockRequestVO);
        return addMockRequest(mockRequestDO);
    }

    @Override
    public boolean addMockRequest(MockRequestDO mockRequestDO) {
        try {
            if (mockRequestDO.getId() == 0) {
                bodyDao.addRequestMock(mockRequestDO);
            } else {
                bodyDao.updateRequestMock(mockRequestDO);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<MockRequestDO> getRequestMockByProtocolId(long protocolId) {
        return bodyDao.getRequestMockByProtocolId(protocolId);
    }

    @Override
    public int delRequestMockByProtocolId(long protocolId) {
        return bodyDao.delRequestMockByProtocolId(protocolId);
    }

    @Override
    public List<MockRequestDO> getAllRequestMock() {
        return bodyDao.getAllRequestMock();
    }

    @Override
    public TablePageVO<List<MockRequestVO>> getRequestMockByProtocolIdPage(long protocolId, long pageStart, int pageLength) {
        long size = bodyDao.getRequestMockByProtocolIdSize(protocolId);
        List<MockRequestDO> requestList = bodyDao.getRequestMockByProtocolIdPage(protocolId, pageStart, pageLength);

        List<MockRequestVO> requestVOList = new ArrayList<>();

        for (MockRequestDO requestPO : requestList) {
            requestVOList.add(mockRequestDOTransformMockRequestVO(requestPO));
        }

        return new TablePageVO<>(requestVOList, size);
    }

    @Override
    public int delRequestMockById(long id) {
        return bodyDao.delRequestMock(id);
    }

    @Override
    public MockRequestVO getRequestMockById(long id) {
        MockRequestDO request = bodyDao.getRequestMockById(id);

        return mockRequestDOTransformMockRequestVO(request);
    }

    /**
     * do到vo的转换
     * @param requestDO
     * @return
     */
    private MockRequestVO mockRequestDOTransformMockRequestVO(MockRequestDO requestDO) {
        HeaderTplDO requestSub = headerTplService.getTplById(requestDO.getRequestHeaderSubscribe());
        HeaderTplVO requestSubVO = requestSub == null ? null : new HeaderTplVO(requestSub);

        HeaderTplDO protocolRequestSub = headerTplService.getTplById(requestDO.getProtocolRequestHeaderSubscribe());
        HeaderTplVO protocolRequestSubVO = protocolRequestSub == null ? null : new HeaderTplVO(protocolRequestSub);

        SystemEnvDO systemEnvDO = systemService.getEnvById(requestDO.getSupportEnv());

        MockRequestVO requestVO = new MockRequestVO(requestDO);
        requestVO.setSupportEnv(systemEnvDO);
        if (requestSubVO != null) {
            requestVO.setRequestHeaderSubscribe(requestSubVO);
            requestVO.setRequestHeader(requestSubVO.getTplValue());
        }

        if (protocolRequestSubVO != null) {
            requestVO.setProtocolRequestHeaderSubscribe(protocolRequestSubVO);
            requestVO.setProtocolRequestHeader(protocolRequestSubVO.getTplValue());
        }

        return requestVO;
    }
}
