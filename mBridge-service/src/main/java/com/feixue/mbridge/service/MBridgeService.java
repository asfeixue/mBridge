package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.meta.domain.HttpProtocol;

import java.util.List;

/**
 * 协议服务
 */
public interface MBridgeService {
    /**
     * 收集协议数据
     * @param protocols
     */
    void collect(List<HttpProtocol> protocols);

    /**
     * 获取合适条件下的协议集合
     * @param url       协议模糊名称
     * @param systemCode 系统编码
     * @param page      当前页
     * @param length    每页协议数目
     * @return
     */
    TablePageVO<List<HttpProtocolVO>> getProtocols(String url, String systemCode, int page, int length);

    /**
     * 查询指定的协议详情
     * @param id
     */
    HttpProtocolVO getProtocolDetail(long id);

    /**
     * 删除指定协议的所有信息
     * @param protocolId
     * @return
     */
    boolean delProtocol(long protocolId);

    /**
     * 保存 or 更新协议约定信息
     * @param protocolVO
     * @return
     */
    boolean saveProtocol(HttpProtocolVO protocolVO);
}

