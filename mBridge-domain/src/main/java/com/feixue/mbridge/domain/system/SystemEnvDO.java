package com.feixue.mbridge.domain.system;

import java.io.Serializable;

public class SystemEnvDO implements Serializable {
    private static final long serialVersionUID = 9036436934759459345L;

    private long id;

    /*
    系统编码
     */
    private String systemCode;

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

    public SystemEnvDO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
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
        return "SystemEnvDO{" +
                "id=" + id +
                ", systemCode='" + systemCode + '\'' +
                ", envName='" + envName + '\'' +
                ", envAddress='" + envAddress + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
