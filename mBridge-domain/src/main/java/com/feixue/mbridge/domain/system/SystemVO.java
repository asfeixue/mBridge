package com.feixue.mbridge.domain.system;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 2017/4/26.
 */
public class SystemVO implements Serializable {
    private static final long serialVersionUID = 3661797494366955411L;

    private long id;

    /*
    系统名称
     */
    private String systemName;

    /*
    系统code
     */
    private String systemCode;

    /*
    负责人
     */
    private String principalUser;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更新时间
     */
    private String gmtModify;

    /*
    环境集合
     */
    private List<SystemEnvDO> envList;

    public SystemVO() {
    }

    public SystemVO(SystemDO systemDO, List<SystemEnvDO> envList) {
        this.id = systemDO.getId();
        this.systemName = systemDO.getSystemName();
        this.systemCode = systemDO.getSystemCode();
        this.principalUser = systemDO.getPrincipalUser();
        this.gmtCreate = systemDO.getGmtCreate();
        this.gmtModify = systemDO.getGmtModify();

        this.envList = envList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getPrincipalUser() {
        return principalUser;
    }

    public void setPrincipalUser(String principalUser) {
        this.principalUser = principalUser;
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

    public List<SystemEnvDO> getEnvList() {
        return envList;
    }

    public void setEnvList(List<SystemEnvDO> envList) {
        this.envList = envList;
    }
}
