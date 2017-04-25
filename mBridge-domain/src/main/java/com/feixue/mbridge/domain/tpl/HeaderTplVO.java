package com.feixue.mbridge.domain.tpl;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 16/7/10.
 */
public class HeaderTplVO implements Serializable {
    private static final long serialVersionUID = -3814741637446464050L;

    private long id;

    /*
    模板值
     */
    private List<ProtocolHeader> tplValue;

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

    public HeaderTplVO() {
    }

    public HeaderTplVO(HeaderTplDO tplDO) {
        this.id = tplDO.getId();
        this.gmtCreate = tplDO.getGmtCreate();
        this.gmtModify = tplDO.getGmtModify();
        this.tplDesc = tplDO.getTplDesc();
        this.tplValue = JSON.parseArray(tplDO.getTplValue(), ProtocolHeader.class);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ProtocolHeader> getTplValue() {
        return tplValue;
    }

    public void setTplValue(List<ProtocolHeader> tplValue) {
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
        return "HeaderTplVO{" +
                "id=" + id +
                ", tplValue=" + tplValue +
                ", tplDesc='" + tplDesc + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
