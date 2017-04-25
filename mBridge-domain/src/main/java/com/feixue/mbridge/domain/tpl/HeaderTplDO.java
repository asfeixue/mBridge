package com.feixue.mbridge.domain.tpl;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/5/7.
 */
public class HeaderTplDO implements Serializable {
    private static final long serialVersionUID = 2426806908121427022L;

    private long id;

    /*
    模板值
     */
    private String tplValue;

    /*
    模板描述
     */
    private String tplDesc;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更新时间
     */
    private String gmtModify;

    public HeaderTplDO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTplValue() {
        return tplValue;
    }

    public void setTplValue(String tplValue) {
        this.tplValue = tplValue;
    }

    public String getTplDesc() {
        return tplDesc;
    }

    public void setTplDesc(String tplDesc) {
        this.tplDesc = tplDesc;
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
        return "HeaderTplDO{" +
                "id=" + id +
                ", tplValue='" + tplValue + '\'' +
                ", tplDesc='" + tplDesc + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
