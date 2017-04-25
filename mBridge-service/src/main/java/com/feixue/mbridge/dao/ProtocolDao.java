package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 15/10/29.
 */
@Component
public interface ProtocolDao {

    /**
     * 获取指定协议信息
     * @param urlPath
     * @param systemCode
     * @return
     */
    HttpProtocolDO getProtocolInfo(@Param("urlPath") String urlPath,
                                   @Param("systemCode") String systemCode);

    /**
     * 保存协议
     * @param protocolPO
     * @return
     */
    int addProtocol(HttpProtocolDO protocolPO);

    /**
     * 更新指定id的协议信息
     * @param protocolPO
     * @return
     */
    int updateProtocol(HttpProtocolDO protocolPO);

    /**
     * 获取Protocol
     * @param id
     * @return
     */
    HttpProtocolDO getProtocol(@Param("id") long id);

    /**
     * 获取合适条件下的协议集合
     * @param url
     * @param systemCode
     * @param page
     * @param length
     * @return
     */
    List<HttpProtocolDO> getProtocols(
            @Param("url") String url,
            @Param("systemCode") String systemCode,
            @Param("page") int page,
            @Param("length") int length);

    /**
     * 获取合适条件下的协议数目
     * @param url
     * @param systemCode
     * @return
     */
    long getProtocolSize(
            @Param("url") String url,
            @Param("systemCode") String systemCode);

    /**
     * 删除指定协议
     * @param protocolId
     * @return
     */
    int delProtocol(@Param("protocolId") long protocolId);

    /**
     * 删除指定协议的测试报告
     * @param protocolId
     * @return
     */
    int delProtocolTestReport(@Param("protocolId") long protocolId);
}
