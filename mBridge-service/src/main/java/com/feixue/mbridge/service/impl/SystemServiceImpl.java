package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.SystemDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.domain.system.SystemEnvVO;
import com.feixue.mbridge.domain.system.SystemVO;
import com.feixue.mbridge.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

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

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public SystemEnvDO getEnvById(long id) {
        return systemDao.getEnvById(id);
    }

    @Override
    public BusinessWrapper<Boolean> saveSystem(final SystemVO systemVO) {
        return transactionTemplate.execute(new TransactionCallback<BusinessWrapper<Boolean>>() {
            @Override
            public BusinessWrapper<Boolean> doInTransaction(TransactionStatus status) {
                try {
                    SystemDO systemDO = new SystemDO(systemVO);
                    if (systemDO.getId() == 0) {
                        systemDao.addSystem(systemDO);
                    } else {
                        systemDao.updateSystem(systemDO);
                    }

                    systemDao.delSystemEnv(systemDO.getSystemCode());
                    if (systemVO.getEnvList() != null && !systemVO.getEnvList().isEmpty()) {
                        systemDao.addServerEnv(systemVO.getEnvList());
                    }

                    return new BusinessWrapper<>(true);
                } catch (Exception e) {
                    status.setRollbackOnly();
                    return new BusinessWrapper<>(ErrorCode.serviceFailure);
                }
            }
        });
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
    public TablePageVO<List<SystemVO>> getSystemPage(String systemCode, int page, int length) {
        List<SystemDO> systemDOList = systemDao.getSystemPage(systemCode, page * length, length);
        long size = systemDao.getSystemSize(systemCode);

        List<SystemVO> systemVOList = new ArrayList<>();

        for(SystemDO systemDO : systemDOList) {
            List<SystemEnvDO> envList = systemDao.getSystemEnvList(systemDO.getSystemCode());

            SystemVO systemVO = new SystemVO(systemDO, envList);

            systemVOList.add(systemVO);
        }

        return new TablePageVO(systemVOList, size);
    }

    @Override
    public List<SystemDO> getSystemList() {
        return systemDao.getSystemList();
    }
}
