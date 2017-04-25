package com.feixue.mbridge.domain.server;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/30.
 */
public class ServerDO implements Serializable {
    private static final long serialVersionUID = 8511285632516970832L;

    private long id;

    /*
    系统编码
     */
    private String systemCode;

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
    创建时间
     */
    private String gmtCreate;

    /*
    更改时间
     */
    private String gmtModify;

    public ServerDO() {
    }

    public ServerDO(ServerVO serverVO) {
        this.id = serverVO.getId();
        this.systemCode = serverVO.getSystemDO().getSystemCode();
        this.serverPort = serverVO.getServerPort();
        this.gmtCreate = serverVO.getGmtCreate();
        this.gmtModify = serverVO.getGmtModify();
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
        return "ServerDO{" +
                "id=" + id +
                ", systemCode='" + systemCode + '\'' +
                ", serverPort=" + serverPort +
                ", serverStatus=" + serverStatus +
                ", serverMsg='" + serverMsg + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public enum ServerStatusEnum {
        statusStop(0, "未启动"),
        statusStart(1, "已启动");
        int code;
        String desc;

        ServerStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public String getCodeString(int code) {
            for(ServerStatusEnum statusEnum : ServerStatusEnum.values()) {
                if (statusEnum.getCode() == code) {
                    return statusEnum.getDesc();
                }
            }
            return "未知";
        }
    }
}
