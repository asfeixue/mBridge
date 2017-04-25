package com.feixue.mbridge.domain.response;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.domain.tpl.HeaderTplVO;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by zxxiao on 16/7/2.
 */
public class MockResponseVO implements Serializable {
    private static final long serialVersionUID = -8728472273743797877L;

    private long id;

    /*
    协议编号
     */
    private long protocolId;

    /*
    协议体
     */
    private String body;

    /*
    协议头
     */
    private List<ProtocolHeader> header;

    /*
    header 订阅
     */
    private HeaderTplVO headerSubscribe;

    /*
    0:不是当前响应；1:是当前响应
     */
    private int isNow;

    /*
    mock描述
     */
    private String mockDesc;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更新时间
     */
    private String gmtModify;

    public MockResponseVO() {
    }

    public MockResponseVO(MockResponseDO responseDO) {
        this.id = responseDO.getId();
        this.protocolId = responseDO.getProtocolId();
        this.body = responseDO.getBody();

        if (responseDO.getHeaderSubscribe() == 0) {
            this.header = responseDO.getHeader() == null ? Collections.EMPTY_LIST : JSON.parseArray(responseDO.getHeader(), ProtocolHeader.class);
        }

        this.isNow = responseDO.getIsNow();
        this.mockDesc = responseDO.getMockDesc();
        this.gmtCreate = responseDO.getGmtCreate();
        this.gmtModify = responseDO.getGmtModify();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(long protocolId) {
        this.protocolId = protocolId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<ProtocolHeader> getHeader() {
        return header;
    }

    public void setHeader(List<ProtocolHeader> header) {
        this.header = header;
    }

    public HeaderTplVO getHeaderSubscribe() {
        return headerSubscribe;
    }

    public void setHeaderSubscribe(HeaderTplVO headerSubscribe) {
        this.headerSubscribe = headerSubscribe;
    }

    public int getIsNow() {
        return isNow;
    }

    public void setIsNow(int isNow) {
        this.isNow = isNow;
    }

    public String getMockDesc() {
        return mockDesc;
    }

    public void setMockDesc(String mockDesc) {
        this.mockDesc = mockDesc;
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
        return "MockResponseVO{" +
                "id=" + id +
                ", protocolId=" + protocolId +
                ", body='" + body + '\'' +
                ", header=" + header +
                ", headerSubscribe=" + headerSubscribe +
                ", isNow=" + isNow +
                ", mockDesc='" + mockDesc + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
