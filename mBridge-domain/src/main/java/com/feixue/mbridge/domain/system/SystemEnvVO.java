package com.feixue.mbridge.domain.system;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/10/14.
 */
public class SystemEnvVO implements Serializable {
    private static final long serialVersionUID = -8108333363480313883L;

    private long id;

    /*
    系统编码
     */
    private SystemDO systemDO;

    /*
    环境名称
     */
    private String envName;

    /*
    环境地址
     */
    private String envAddress;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    修改时间
     */
    private String gmtModify;

    public SystemEnvVO(SystemEnvDO envDO, SystemDO systemDO) {
        this.id = envDO.getId();
        this.systemDO = systemDO;
        this.envName = envDO.getEnvName();
        this.envAddress = envDO.getEnvAddress();
        this.gmtCreate = envDO.getGmtCreate();
        this.gmtModify = envDO.getGmtModify();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SystemDO getSystemDO() {
        return systemDO;
    }

    public void setSystemDO(SystemDO systemDO) {
        this.systemDO = systemDO;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getEnvAddress() {
        return envAddress;
    }

    public void setEnvAddress(String envAddress) {
        this.envAddress = envAddress;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "SystemEnvVO{" +
                "id=" + id +
                ", systemDO=" + systemDO +
                ", envName='" + envName + '\'' +
                ", envAddress='" + envAddress + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
