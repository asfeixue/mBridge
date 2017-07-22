package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.ServerDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.server.ServerDO;
import com.feixue.mbridge.domain.server.ServerVO;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.service.ServerService;
import com.feixue.mbridge.service.SystemService;
import com.feixue.mbridge.util.IPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 16/9/30.
 */
@Service
public class ServerServiceImpl implements ServerService {

    private static final Logger logger = LoggerFactory.getLogger(ServerServiceImpl.class);

    @Resource
    private ServerDao serverDao;

    @Resource
    private SystemService systemService;

    @Override
    public TablePageVO<List<ServerVO>> queryServer(String systemCode, int page, int length) {
        long size = serverDao.queryServerSize(systemCode);
        List<ServerDO> serverDOList = serverDao.queryServerPage(systemCode, page * length, length);

        List<ServerVO> serverVOList = new ArrayList<>();
        for(ServerDO serverDO : serverDOList) {
            SystemDO systemDO = systemService.querySystem(serverDO.getSystemCode());

            ServerVO serverVO = new ServerVO(serverDO, systemDO, "http://" + IPUtil.getLocalHostIP() + ":" + serverDO.getServerPort());

            serverVOList.add(serverVO);
        }

        return new TablePageVO<>(serverVOList, size);
    }

    @Override
    public BusinessWrapper<Boolean> addServer(ServerVO serverVO) {
        try {
            if (serverVO.getServerPort() <= 1024) {
                return new BusinessWrapper<>(ErrorCode.portNoPermission);
            }
            ServerDO serverDO = new ServerDO(serverVO);
            serverDao.addServer(serverDO);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serviceFailure);
        }
    }

    @Override
    public BusinessWrapper<Boolean> delServer(long serverId) {
        try {
            serverDao.delServer(serverId);
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serviceFailure);
        }
    }
}
