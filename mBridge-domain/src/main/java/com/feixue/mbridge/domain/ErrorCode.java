package com.feixue.mbridge.domain;

/**
 * Created by zxxiao on 16/6/13.
 */
public enum ErrorCode {
    canNotDelSystem("0001", "系统有关联协议，暂不能删除！"),
    canNotDelWithNoSystem("0002", "不能删除不存在的系统！"),
    canNotDelSystemEnv("0003", "系统环境删除失败！"),
    protocolNotExist("0004", "协议不存在！"),
    systemNotExist("0005", "系统不存在！"),
    systemExist("0006", "系统已存在！"),
    protocolNowResponseNotExist("0007", "协议当前约定响应不存在！"),
    protocolNoNowResponse("0008", "请求协议未指定当前mock响应报文！"),
    workflowNotExist("0009", "任务流不存在！"),
    paramNotMatch("0010", "参数不匹配！"),
    serverNotExist("0011", "指定服务器不存在!"),
    portNoPermission("0012", "无权限使用此端口!"),
    addFailure("9997", "新增失败！"),
    updateFailure("9998", "更新失败！"),
    serviceFailure("9999", "服务异常！");
    private String code;
    private String msg;

    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
