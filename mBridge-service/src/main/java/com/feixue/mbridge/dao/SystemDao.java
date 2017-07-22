package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.system.SystemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/5/16.
 */
@Component
public interface SystemDao {

    /**
     * 获取指定系统的所有环境信息
     * @param systemCode
     * @return
     */
    List<SystemEnvDO> getSystemEnvList(@Param("systemCode") String systemCode);

    /**
     * 添加指定系统的环境信息
     * @param envList
     * @param systemCode
     * @return
     */
    boolean addServerEnv(@Param("list")List<SystemEnvDO> envList, @Param("systemCode") String systemCode);

    /**
     * 删除指定系统的环境信息
     * @param systemCode
     * @return
     */
    boolean delSystemEnv(@Param("systemCode") String systemCode);

    /**
     * 获取指定id的env信息
     * @param id
     * @return
     */
    SystemEnvDO getEnvById(@Param("id") long id);

    /**
     * 添加系统
     * @param systemDO
     * @return
     */
    int addSystem(SystemDO systemDO);

    /**
     * 更新系统信息
     * @param systemDO
     * @return
     */
    int updateSystem(SystemDO systemDO);

    /**
     * 删除系统
     * @param systemCode
     * @return
     */
    int delSystem(@Param("systemCode") String systemCode);

    /**
     * 查询指定code的系统信息
     * @param systemCode
     * @return
     */
    SystemDO querySystem(@Param("systemCode") String systemCode);

    /**
     * 获取指定系统协议数目
     * @param systemCode
     * @return
     */
    int getSystemProtocols(@Param("systemCode") String systemCode);

    /**
     * 获取系统数目
     * @return
     */
    long getSystemSize(@Param("systemCode") String systemCode);

    /**
     * 获取指定page的系统信息
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<SystemDO> getSystemPage(
            @Param("systemCode") String systemCode, @Param("pageStart") long pageStart,
            @Param("pageLength") int pageLength);

    /**
     * 获取所有系统
     * @return
     */
    List<SystemDO> getSystemList();
}
