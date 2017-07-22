package com.feixue.mbridge.service.impl;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.dao.ProtocolDao;
import com.feixue.mbridge.dao.SystemDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.domain.proxy.HttpProxyContent;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.endpoint.ServerProxy;
import com.feixue.mbridge.proxy.CallbackNotify;
import com.feixue.mbridge.proxy.ProxyManager;
import com.feixue.mbridge.proxy.http.HttpProxyManager;
import com.feixue.mbridge.proxy.http.HttpServerProxy;
import com.feixue.mbridge.service.ProxyService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zxxiao on 2017/7/19.
 */
@Service
public class ProxyServiceImpl implements ProxyService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServiceImpl.class);

    @Resource
    private SystemDao systemDao;

    @Resource
    private ProtocolDao protocolDao;

    @Override
    public boolean registerServer(final String systemCode) {
        SystemDO systemDO = systemDao.querySystem(systemCode);
        //目前默认是http proxy server
        final ProxyManager proxyManager = HttpProxyManager.getInstance();
        return proxyManager.registerProxy(systemDO, new CallbackNotify<HttpProxyContent, BusinessWrapper>() {
            @Override
            public boolean doNotify(HttpProxyContent content, BusinessWrapper wrapper) {
                /**
                 * 1.提取上下文，进行校验
                 * 2.存储校验结果，响应指定内容
                 */
                if (!canProcess(systemCode, content.getUrl())) {
                    wrapper.setSuccess(false);
                    wrapper.setCode(ErrorCode.protocolNotExist.getCode());
                    wrapper.setMsg(ErrorCode.protocolNotExist.getMsg());
                    return false;
                }
                String queryUrl = content.getUrl().substring(("/" + systemCode).length());
                HttpProtocolDO protocolDO = protocolDao.getProtocolInfo(queryUrl, systemCode);

                if (protocolDO == null) {
                    wrapper.setSuccess(false);
                    wrapper.setCode(ErrorCode.protocolNotExist.getCode());
                    wrapper.setMsg(ErrorCode.protocolNotExist.getMsg());
                    return false;
                }
                HttpProxyContent response = new HttpProxyContent();
                if (!StringUtils.isEmpty(protocolDO.getResponseHeader())) {
                    response.setHeadersMap(JSON.parseObject(protocolDO.getResponseHeader(), Map.class));
                }
                response.setBody(protocolDO.getResponseBody());
                if (StringUtils.isEmpty(protocolDO.getContentType())) {
                    response.setContentType(ContentType.TEXT_PLAIN.toString());
                } else {
                    response.setContentType(ContentType.APPLICATION_JSON.toString());
                }

                wrapper.setBody(response);
                wrapper.setSuccess(true);
                return true;
            }
        });
    }

    @Override
    public boolean unregisterServer(String systemCode) {
        SystemDO systemDO = systemDao.querySystem(systemCode);
        //目前默认是http proxy server
        final ProxyManager proxyManager = HttpProxyManager.getInstance();
        return proxyManager.unregisterProxy(systemDO);
    }

    /**
     * 判断是否可以处理
     * @param systemCode
     * @param url
     * @return
     */
    private boolean canProcess(String systemCode, String url) {
        String rootPath = "/" + systemCode;
        if (url.startsWith(rootPath)) {
            return true;
        } else {
            return false;
        }
    }
}
