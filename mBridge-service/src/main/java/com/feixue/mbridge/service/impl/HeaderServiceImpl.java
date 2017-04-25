package com.feixue.mbridge.service.impl;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.dao.HeaderDao;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.domain.tpl.HeaderTplDO;
import com.feixue.mbridge.service.HeaderService;
import com.feixue.mbridge.service.HeaderTplService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 16/5/31.
 */
@Service
public class HeaderServiceImpl implements HeaderService {

    @Resource
    private HeaderDao headerDao;

    @Resource
    private HeaderTplService headerTplService;

    @Override
    public void saveHeader(int headerType, int index, long protocolId, List<ProtocolHeader> protocolHeaderList, long headerId) {
        switch (headerType) {
            case ProtocolHeader.HeaderType.protocol:
                headerDao.updateProtocolHeaders(index, protocolId, JSON.toJSONString(protocolHeaderList), headerId);
                break;
        }
    }

    @Override
    public Map<String, Object> getTypeHeaders(long id) {
        Map<String, Object> headerMap = new HashMap<>();
        HttpProtocolDO httpProtocolDO = headerDao.getProtocolHeaders(id);

        long requestSub = httpProtocolDO.getRequestHeaderSubscribe();
        Map<String, Object> requestMap = new HashMap<>();
        String request;
        if (requestSub == 0) {
            requestMap.put("id", 0);
            requestMap.put("tplDesc", "");
            request = httpProtocolDO.getRequestHeader();
        } else {
            HeaderTplDO requestTpl = headerTplService.getTplById(requestSub);

            requestMap.put("id", requestTpl.getId());
            requestMap.put("tplDesc", requestTpl.getTplDesc());
            request = requestTpl.getTplValue();
        }

        long responseSub = httpProtocolDO.getResponseHeaderSubscribe();
        Map<String, Object> responseMap = new HashMap<>();
        String response;
        if (responseSub == 0) {
            responseMap.put("id", 0);
            responseMap.put("tplDesc", "");
            response = httpProtocolDO.getResponseHeader();
        } else {
            HeaderTplDO responseTpl = headerTplService.getTplById(responseSub);

            responseMap.put("id", responseTpl.getId());
            responseMap.put("tplDesc", responseTpl.getTplDesc());
            response = responseTpl.getTplValue();
        }

        headerMap.put("left", request);
        headerMap.put("leftSub", requestMap);
        headerMap.put("right", response);
        headerMap.put("rightSub", responseMap);
        return headerMap;
    }
}
