package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.BridgeDao;
import com.feixue.mbridge.domain.bridge.BridgeInfoPO;
import com.feixue.mbridge.domain.bridge.BridgeInfoVO;
import com.feixue.mbridge.service.BridgeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 16/6/19.
 */
@Service
public class BridgeServiceImpl implements BridgeService {

    @Resource
    private BridgeDao bridgeDao;

    @Override
    public boolean addBridge(BridgeInfoVO bridgeInfoVO) {
        BridgeInfoPO infoPO = new BridgeInfoPO(bridgeInfoVO);
        return bridgeDao.addBridge(infoPO);
    }
}
