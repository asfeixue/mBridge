package com.feixue.mbridge.dao;

import com.feixue.mbridge.domain.tpl.HeaderTplDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zxxiao on 16/5/7.
 */
@Component
public interface HeaderTplDao {

    /**
     * 添加header模板
     * @param headerTplDO
     * @return
     */
    boolean addHeaderTpl(HeaderTplDO headerTplDO);

    /**
     * 获取header模板总记录数
     * @return
     */
    int getHeaderTplSize();

    /**
     * 获取header模板分页数据
     * @param pageStart
     * @param pageLength
     * @return
     */
    List<HeaderTplDO> getHeaderTplPage(@Param("pageStart") int pageStart, @Param("pageLength") int pageLength);

    /**
     * 删除指定的模板
     * @param headerTplDO
     * @return
     */
    boolean delTpl(HeaderTplDO headerTplDO);

    /**
     * 更新模板内容
     * @param headerTplDO
     * @return
     */
    boolean updateTpl(HeaderTplDO headerTplDO);

    /**
     * 查询指定编号的模板
     * @param id
     * @return
     */
    HeaderTplDO getTplById(@Param("id") long id);
}
