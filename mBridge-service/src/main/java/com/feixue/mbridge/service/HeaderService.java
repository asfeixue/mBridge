package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.protocol.ProtocolHeader;

import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 16/5/31.
 */
public interface HeaderService {
    /**
     * 保存指定类型的指定位置的协议header
     * @param headerType
     * @param index
     * @param protocolId
     * @param protocolHeaderList
     * @param headerId
     */
    void saveHeader(int headerType, int index, long protocolId, List<ProtocolHeader> protocolHeaderList, long headerId);

    /**
     * 获取指定协议，指定组的header集合
     * @param id
     * @return
     */
    Map<String, Object> getTypeHeaders(long id);
}
