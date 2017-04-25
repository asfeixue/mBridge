package com.feixue.mbridge.service.impl;

import com.feixue.mbridge.dao.HeaderTplDao;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.tpl.HeaderTplDO;
import com.feixue.mbridge.domain.tpl.HeaderTplVO;
import com.feixue.mbridge.service.HeaderTplService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxxiao on 16/5/7.
 */
@Service("headerTplService")
public class HeaderTplServiceImpl implements HeaderTplService {

    private static final Logger logger = LoggerFactory.getLogger(HeaderTplServiceImpl.class);

    @Resource
    private HeaderTplDao headerTplDao;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public boolean addHeaderTpl(final HeaderTplDO headerTplDO) {
        HeaderTplDO localTpl = headerTplDao.getTplById(headerTplDO.getId());
        if (localTpl != null) {
            return headerTplDao.updateTpl(headerTplDO);
        } else {
            return headerTplDao.addHeaderTpl(headerTplDO);
        }
    }

    @Override
    public TablePageVO<List<HeaderTplVO>> getHeaderTplPage(int pageStart, int pageLength) {
        int size = headerTplDao.getHeaderTplSize();
        List<HeaderTplDO> tplList = headerTplDao.getHeaderTplPage(pageStart * pageLength, pageLength);

        List<HeaderTplVO> voList = new ArrayList<>();
        for(HeaderTplDO tplDO : tplList) {
            HeaderTplVO tplVO = new HeaderTplVO(tplDO);
            voList.add(tplVO);
        }

        return new TablePageVO<>(voList, size);
    }

    @Override
    public boolean delTpl(HeaderTplDO headerTplDO) {
        return headerTplDao.delTpl(headerTplDO);
    }

    @Override
    public HeaderTplDO getTplById(long id) {
        return headerTplDao.getTplById(id);
    }
}
