package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.domain.system.SystemEnvVO;
import com.feixue.mbridge.domain.system.SystemVO;

import java.util.List;

/**
 * Created by zxxiao on 16/5/16.
 */
public interface SystemService {

    /**
     * 获取指定id的env信息
     * @param id
     * @return
     */
    SystemEnvDO getEnvById(long id);

    /**
     * 新增 or 更新系统信息
     * @param systemVO
     * @return
     */
    BusinessWrapper<Boolean> saveSystem(SystemVO systemVO);

    /**
     * 删除系统
     * @param systemCode
     * @return
     */
    BusinessWrapper<Boolean> delSystem(String systemCode);

    /**
     * 查询指定code的系统信息
     * @param systemCode
     * @return
     */
    SystemDO querySystem(String systemCode);

    /**
     * 获取所有系统
     * @return
     */
    TablePageVO<List<SystemVO>> getSystemPage(String systemCode, int page, int length);

    /**
     * 获取所有系统
     * @return
     */
    List<SystemDO> getSystemList();
}
