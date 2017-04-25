package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.server.ServerVO;

import java.util.List;

/**
 * Created by zxxiao on 16/9/30.
 */
public interface ServerService {

    /**
     * 查询托管指定系统的mock server
     * @param systemCode
     * @param page
     * @param length
     * @return
     */
    TablePageVO<List<ServerVO>> queryServer(String systemCode, int page, int length);

    /**
     * 新增托管协议的mock server
     * @param serverVO
     * @return
     */
    BusinessWrapper<Boolean> addServer(ServerVO serverVO);

    /**
     * 删除指定的mock server
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> delServer(long serverId);

    /**
     * 启动指定的mock server
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> startServer(long serverId);

    /**
     * 停止指定的mock server
     * @param serverId
     * @return
     */
    BusinessWrapper<Boolean> stopServer(long serverId);
}
