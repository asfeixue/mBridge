package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * Created by zxxiao on 15/12/20.
 */
@Component
public interface HeaderDao {

    /**
     * 获取协议约定header
     * @param protocolId
     * @return
     */
    HttpProtocolDO getProtocolHeaders(@Param("protocolId") long protocolId);

    /**
     * 更新协议约定header
     * @param index
     * @param protocolId
     * @param header
     * @param headerId
     * @return
     */
    int updateProtocolHeaders(@Param("index") int index,
                              @Param("protocolId") long protocolId,
                              @Param("header") String header,
                              @Param("headerId") long headerId);
}
