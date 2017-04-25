package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.bridge.BridgeInfoPO;
import org.springframework.stereotype.Component;

/**
 * Created by zxxiao on 16/6/19.
 */
@Component
public interface BridgeDao {

    /**
     * 新增新的桥接数据
     * @param infoPO
     * @return
     */
    boolean addBridge(BridgeInfoPO infoPO);
}
