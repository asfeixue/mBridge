package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.request.MockRequestDO;
import com.feixue.mbridge.domain.response.MockResponseDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 15/12/20.
 */
@Component
public interface BodyDao {

    /**
     * 获取指定协议的当前响应
     * @param protocolId
     * @return
     */
    MockResponseDO getProtocolNowResponse(@Param("protocolId") long protocolId);

    /**
     * 更新指定id为当前mock responseBody
     * @param isNow
     * @param id
     * @return
     */
    boolean updateMockResponseNow(@Param("isNow") int isNow, @Param("id") long id);

    /**
     * 更新指定protocolId的mock responseBody都不为当前响应
     * @param protocolId
     * @return
     */
    int updateProtocolMockResponseIsNotNow(@Param("protocolId") long protocolId);

    /**
     * 新增mock responsebody记录
     * @param mockResponseDO
     * @return
     */
    int addMockResponse(MockResponseDO mockResponseDO);

    /**
     * 更新指定编号的模拟响应
     * @param mockResponseDO
     * @return
     */
    int updateMockResponseById(MockResponseDO mockResponseDO);

    /**
     * 删除指定编号的mock response body
     * @param id
     * @return
     */
    boolean delMockResponseBody(@Param("id") long id);

    /**
     * 获取指定协议的mock responseBody数量
     * @param protocolId
     * @return
     */
    long getMockResponseSize(@Param("protocolId") long protocolId);

    /**
     * 获取指定协议的mock responseBody分页数据
     * @param protocolId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<MockResponseDO> getMockResponsePage(
            @Param("protocolId") long protocolId,
            @Param("pageStart") int pageStart, @Param("pageLength") int pageLength);

    /**
     * 添加request mock
     * @param mockRequestDO
     * @return
     */
    int addRequestMock(MockRequestDO mockRequestDO);

    /**
     * 删除指定id的request mock
     * @param id
     * @return
     */
    int delRequestMock(@Param("id") long id);

    /**
     * 获取指定id的request mock
     * @param id
     * @return
     */
    MockRequestDO getRequestMockById(@Param("id") long id);

    /**
     * 更新指定id的request mock
     * @param mockRequestDO
     * @return
     */
    int updateRequestMock(MockRequestDO mockRequestDO);

    /**
     * 获取指定协议的request mock集合
     * @param protocolId
     * @return
     */
    List<MockRequestDO> getRequestMockByProtocolId(@Param("protocolId") long protocolId);

    /**
     * 删除指定协议id的request mock
     * @param protocolId
     * @return
     */
    int delRequestMockByProtocolId(@Param("protocolId") long protocolId);

    /**
     * 获取所有request mock协议
     * @return
     */
    List<MockRequestDO> getAllRequestMock();

    /**
     * 获取指定协议的request mock数量
     * @param protocolId
     * @return
     */
    long getRequestMockByProtocolIdSize(@Param("protocolId") long protocolId);

    /**
     * 获取指定协议的request mock分页数据
     * @param protocolId
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<MockRequestDO> getRequestMockByProtocolIdPage(@Param("protocolId") long protocolId,
                                                     @Param("pageStart") long pageStart,
                                                     @Param("pageLength") int pageLength);
}
