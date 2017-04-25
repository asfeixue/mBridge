package com.feixue.mbridge.service;

import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.tpl.HeaderTplDO;
import com.feixue.mbridge.domain.tpl.HeaderTplVO;

import java.util.List;

/**
 * Created by zxxiao on 16/5/7.
 */
public interface HeaderTplService {

    /**
     * 添加header模板
     * @param headerTplDO
     * @return
     */
    boolean addHeaderTpl(HeaderTplDO headerTplDO);

    /**
     * 获取header模板分页数据
     * @param pageStart
     * @param pageLength
     * @return
     */
    TablePageVO<List<HeaderTplVO>> getHeaderTplPage(int pageStart, int pageLength);

    /**
     * 删除指定的模板
     * @param headerTplDO
     * @return
     */
    boolean delTpl(HeaderTplDO headerTplDO);

    /**
     * 查询指定编号的模板
     * @param id
     * @return
     */
    HeaderTplDO getTplById(long id);
}
