package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.bridge.BridgeInfoVO;

/**
 * Created by zxxiao on 16/6/19.
 */
public interface BridgeService {

    /**
     * 新增新的桥接数据
     * @param bridgeInfoVO
     * @return
     */
    boolean addBridge(BridgeInfoVO bridgeInfoVO);
}
