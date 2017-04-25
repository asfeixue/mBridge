package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.request.MockRequestDO;
import com.feixue.mbridge.domain.request.MockRequestVO;
import com.feixue.mbridge.domain.response.MockResponseDO;
import com.feixue.mbridge.domain.response.MockResponseVO;

import java.util.List;

/**
 * Created by zxxiao on 16/4/26.
 */
public interface BodyService {

    /**
     * 获取指定协议的当前响应
     * @param protocolId
     * @return
     */
    BusinessWrapper<MockResponseVO> getProtocolNowResponse(long protocolId);

    /**
     * 新增mock responsebody记录
     * @param mockResponseVO
     * @return
     */
    boolean addMockResponse(MockResponseVO mockResponseVO);

    /**
     * 新增mock responsebody记录
     * @param mockResponseDO
     * @return
     */
    boolean addMockResponse(MockResponseDO mockResponseDO);

    /**
     * 删除指定编号的mock response body
     * @param id
     * @return
     */
    boolean delMockResponse(long id);

    /**
     * 设置模拟响应为当前响应
     * @param id
     * @param protocolId
     * @return
     */
    boolean setMockResponseNow(long id, long protocolId);

    /**
     * 取消模拟响应为当前响应
     * @param id
     * @param protocolId
     * @return
     */
    boolean setMockResponseNotNow(long id, long protocolId);

    /**
     * 获取指定协议的mock responseBody分页数据
     * @param protocolId
     * @param pageStart
     * @param pageLength
     * @return
     */
    TablePageVO<List<MockResponseVO>> getMockResponsePage(long protocolId, int pageStart, int pageLength);

    /**
     * 添加request mock
     * @param mockRequestVO
     * @return
     */
    boolean addMockRequest(MockRequestVO mockRequestVO);

    /**
     * 添加request mock
     * @param mockRequestDO
     * @return
     */
    boolean addMockRequest(MockRequestDO mockRequestDO);

    /**
     * 获取指定协议的request mock集合
     * @param protocolId
     * @return
     */
    List<MockRequestDO> getRequestMockByProtocolId(long protocolId);

    /**
     * 删除指定协议id的request mock
     * @param protocolId
     * @return
     */
    int delRequestMockByProtocolId(long protocolId);

    /**
     * 获取所有request mock协议
     * @return
     */
    List<MockRequestDO> getAllRequestMock();

    /**
     * 获取指定协议的request mock分页数据
     * @param protocolId
     * @param pageStart
     * @param pageLength
     * @return
     */
    TablePageVO<List<MockRequestVO>> getRequestMockByProtocolIdPage(long protocolId, long pageStart, int pageLength);

    /**
     * 删除指定id的request mock
     * @param id
     * @return
     */
    int delRequestMockById(long id);

    /**
     * 获取指定id的request mock
     * @param id
     * @return
     */
    MockRequestVO getRequestMockById(long id);
}
