package com.feixue.mbridge.domain.response;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/4/26.
 */
public class MockResponseDO implements Serializable {
    private static final long serialVersionUID = -6066269280249126689L;

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
    private String header;

    /*
    header订阅
     */
    private long headerSubscribe;

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

    public MockResponseDO() {
    }

    public MockResponseDO(MockResponseVO mockResponseVO) {
        this.id = mockResponseVO.getId();
        this.protocolId = mockResponseVO.getProtocolId();
        this.body = mockResponseVO.getBody();
        this.isNow = mockResponseVO.getIsNow();
        this.header = JSON.toJSONString(mockResponseVO.getHeader());
        this.headerSubscribe = mockResponseVO.getHeaderSubscribe() == null ? 0 : mockResponseVO.getHeaderSubscribe().getId();
        this.mockDesc = mockResponseVO.getMockDesc();
    }

    public MockResponseDO(HttpProtocolDO protocolPO) {
        this.protocolId = protocolPO.getId();
        this.body = protocolPO.getResponseBody();
        this.mockDesc = "default";
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public long getHeaderSubscribe() {
        return headerSubscribe;
    }

    public void setHeaderSubscribe(long headerSubscribe) {
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
        return "MockResponseDO{" +
                "id=" + id +
                ", protocolId=" + protocolId +
                ", body='" + body + '\'' +
                ", header='" + header + '\'' +
                ", headerSubscribe=" + headerSubscribe +
                ", isNow=" + isNow +
                ", mockDesc='" + mockDesc + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public enum IsNow {
        now(1),
        notNow(0);
        private int status;

        private IsNow(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
    }
}
