package com.feixue.mbridge.domain.server;

import com.feixue.mbridge.domain.system.SystemDO;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/30.
 */
public class ServerVO implements Serializable {
    private static final long serialVersionUID = 5835950954862864819L;

    private long id;

    /*
    系统
     */
    private SystemDO systemDO;

    /*
    server端口
     */
    private int serverPort;

    /*
    server状态。0：未启动；1：已启动
     */
    private int serverStatus;

    /*
    错误描述信息
     */
    private String serverMsg;

    /*
    访问地址
     */
    private String serverAddress;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更改时间
     */
    private String gmtModify;

    public ServerVO() {
    }

    public ServerVO(ServerDO serverDO, SystemDO systemDO, String serverAddress) {
        this.id = serverDO.getId();
        this.systemDO = systemDO;
        this.serverPort = serverDO.getServerPort();
        this.serverStatus = serverDO.getServerStatus();
        this.serverMsg = serverDO.getServerMsg();
        this.serverAddress = serverAddress;
        this.gmtCreate = serverDO.getGmtCreate();
        this.gmtModify = serverDO.getGmtModify();
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

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
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
        return "ServerVO{" +
                "id=" + id +
                ", systemDO=" + systemDO +
                ", serverPort=" + serverPort +
                ", serverStatus=" + serverStatus +
                ", serverMsg='" + serverMsg + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
