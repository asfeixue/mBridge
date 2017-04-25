package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.server.ServerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/9/30.
 */
@Component
public interface ServerDao {

    /**
     * 获取满足要求的mock server数目
     * @param systemCode
     * @return
     */
    long queryServerSize(@Param("systemCode") String systemCode);

    /**
     * 获取满足要求的mock server分页数据
     * @param systemCode
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<ServerDO> queryServerPage(
            @Param("systemCode") String systemCode,
            @Param("pageStart") long pageStart, @Param("pageLength") int pageLength);

    /**
     * 新增mock server
     * @param serverDO
     * @return
     */
    int addServer(ServerDO serverDO);

    /**
     * 删除mock server
     * @param id
     * @return
     */
    int delServer(@Param("id") long id);

    /**
     * 获取指定id的Server对象
     * @param id
     * @return
     */
    ServerDO queryServerById(@Param("id") long id);

    /**
     * 更新mock server
     * @param serverDO
     * @return
     */
    int updateServer(ServerDO serverDO);
}
