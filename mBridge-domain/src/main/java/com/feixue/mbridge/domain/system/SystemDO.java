package com.feixue.mbridge.domain.system;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/6/12.
 */
public class SystemDO implements Serializable {
    private static final long serialVersionUID = 1992760778345790375L;

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

    public SystemDO() {}

    public SystemDO(SystemVO systemVO) {
        this.id = systemVO.getId();
        this.systemName = systemVO.getSystemName();
        this.systemCode = systemVO.getSystemCode();
        this.principalUser = systemVO.getPrincipalUser();
        this.gmtCreate = systemVO.getGmtCreate();
        this.gmtModify = systemVO.getGmtModify();
    }

    public SystemDO(String systemCode) {
        this.systemCode = systemCode;
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
}
