package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.SystemDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.domain.system.SystemEnvVO;
import com.feixue.mbridge.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 16/5/16.
 */
@Service
public class SystemServiceImpl implements SystemService {

    private static final Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);

    @Resource
    private SystemDao systemDao;

    @Override
    public TablePageVO<List<SystemEnvVO>> getSystemEnv(String systemCode, int page, int length) {
        long size = systemDao.getServerEnvSize(systemCode);
        List<SystemEnvDO> envDOList = systemDao.getServerEnvPage(systemCode, page * length, length);

        List<SystemEnvVO> envVOList = new ArrayList<>();
        for(SystemEnvDO envDO : envDOList) {
            SystemDO systemDO = systemDao.querySystem(envDO.getSystemCode());

            SystemEnvVO envVO = new SystemEnvVO(envDO, systemDO);
            envVOList.add(envVO);
        }

        return new TablePageVO<>(envVOList, size);
    }

    @Override
    public BusinessWrapper<Boolean> delSystemEnv(long id) {
        boolean result = systemDao.delSystemEnv(id);
        if (result) {
            return new BusinessWrapper<>(true);
        } else {
            return new BusinessWrapper<>(false, ErrorCode.canNotDelSystemEnv);
        }
    }

    @Override
    public BusinessWrapper<Boolean> saveSystemEnv(SystemEnvDO systemEnvDO) {
        try {
            if (systemEnvDO.getId() == 0) {
                systemDao.addServerEnv(systemEnvDO);
            } else {
                systemDao.updateServerEnv(systemEnvDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serviceFailure);
        }
    }

    @Override
    public SystemEnvDO getEnvById(long id) {
        return systemDao.getEnvById(id);
    }

    @Override
    public BusinessWrapper<Boolean> saveSystem(SystemDO systemDO) {
        try {
            if (systemDO.getId() == 0) {
                systemDao.addSystem(systemDO);
            } else {
                systemDao.updateSystem(systemDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            return new BusinessWrapper<>(ErrorCode.serviceFailure);
        }
    }


    @Override
    public BusinessWrapper<Boolean> delSystem(String systemCode) {
        /**
         * 1.检查这个系统是否存在关联协议
         * 2.有，则返回删除失败，响应具体删除失败原因
         * 3.无，则执行删除
         */
        int protocols = systemDao.getSystemProtocols(systemCode);
        if (protocols == 0) {
            int nums = systemDao.delSystem(systemCode);
            if (nums == 0) {
                return new BusinessWrapper<>(false, ErrorCode.canNotDelWithNoSystem);
            } else {
                return new BusinessWrapper<>(true);
            }
        } else {
            return new BusinessWrapper<>(false, ErrorCode.canNotDelSystem);
        }
    }

    @Override
    public SystemDO querySystem(String systemCode) {
        return systemDao.querySystem(systemCode);
    }

    @Override
    public TablePageVO<List<SystemDO>> getSystemPage(String systemCode, int page, int length) {
        List<SystemDO> systemDOList = systemDao.getSystemPage(systemCode, page * length, length);
        long size = systemDao.getSystemSize(systemCode);

        return new TablePageVO(systemDOList, size);
    }

    @Override
    public List<SystemDO> getSystemList() {
        return systemDao.getSystemList();
    }
}
