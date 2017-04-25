package com.feixue.mbridge.endpoint.handler;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.dao.ProtocolDao;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.bridge.BridgeInfoVO;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.service.BridgeService;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by zxxiao on 16/6/19.
 */
public class BridgeHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(BridgeHandler.class);

    /*
    端口
     */
    private int port;

    private BridgeService bridgeService;

    private ProtocolDao protocolDao;

    public BridgeHandler(int port, BridgeService bridgeService, ProtocolDao protocolDao) {
        this.port = port;
        this.bridgeService = bridgeService;
        this.protocolDao = protocolDao;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //非受理端口，不做处理
        if (baseRequest.getServerPort() != port) {
            return;
        }

        String method = request.getMethod();
        if (!method.equalsIgnoreCase("POST")
                && !method.equalsIgnoreCase("GET")
                && !method.equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpStatus.SC_METHOD_NOT_ALLOWED);
            return;
        }

        String targetStr = target.substring(1, target.length());
        int codeIndex = targetStr.indexOf("/");
        if (codeIndex == -1) {
            return;
        }

        String systemCode = targetStr.substring(0, codeIndex);
        String urlPath = targetStr.substring(targetStr.indexOf("/"), targetStr.length());

        HttpProtocolDO protocolPO = protocolDao.getProtocolInfo(urlPath, systemCode);

        BridgeInfoVO infoVO = new BridgeInfoVO();
        infoVO.setUrl(urlPath); //请求路径
        infoVO.setCompleteUrl(baseRequest.getUri().toString()); //完整请求路径
        infoVO.setSystemCode(systemCode);   //系统编码
        infoVO.setHttpMethod(method);   //请求方法

        Enumeration<String> headerEnumeration = request.getHeaderNames();
        List<ProtocolHeader> headerList = new ArrayList<>();
        infoVO.setRequestHeader(headerList);    //请求header

        if (headerEnumeration != null) {
            while (headerEnumeration.hasMoreElements()) {
                String headerName = headerEnumeration.nextElement();
                String headerValue = request.getHeader(headerName);

                ProtocolHeader header;
                if (protocolPO == null) {
                    header = new ProtocolHeader(headerName, headerValue);
                } else {
                    header = new ProtocolHeader(protocolPO.getId(), headerName, headerValue);
                }
                headerList.add(header);
            }
        }

        ContentType contentType = ContentType.create(request.getContentType());
        String body = getRequestBody(request);
        infoVO.setRequestBody(getRequestBody(contentType, body));   //请求body

        String url = baseRequest.getUri().toString();
        BusinessWrapper<HttpResponse> wrapper = requestForwarding(method, url, headerList, contentType, body);

        String responseBody;
        if (wrapper.isSuccess()) {
            HttpResponse forwardResponse = wrapper.getBody();

            HttpEntity entity = forwardResponse.getEntity();

            ContentType responseContentType = ContentType.parse(entity.getContentType().getValue());    //响应媒体类型
            response.setContentType(responseContentType.toString());

            Header characterEncoding = entity.getContentEncoding();
            if (characterEncoding != null) {
                response.setCharacterEncoding(characterEncoding.getValue());
            }

            List<ProtocolHeader> responseHeaderList = new ArrayList<>();
            infoVO.setResponseHeader(responseHeaderList);   //响应header
            for(Header header : forwardResponse.getAllHeaders()) {
                response.addHeader(header.getName(), header.getValue());
                ProtocolHeader protocolHeader = new ProtocolHeader(header.getName(), header.getValue());
                responseHeaderList.add(protocolHeader);
            }

            int statusCode = forwardResponse.getStatusLine().getStatusCode();
            infoVO.setHttpStatus(statusCode);   //http status
            response.setStatus(statusCode);

            responseBody = getResponseBody(entity);
            infoVO.setResponseBody(responseBody);   //响应实体

            saveBridgeInfo(contentType, responseContentType, infoVO);
        } else {
            responseBody = wrapper.getMsg();
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
        response.getWriter().write(responseBody);
        response.flushBuffer();
    }

    /**
     * 保存桥接信息
     * @param contentType
     * @param responseContentType
     * @param infoVO
     */
    private void saveBridgeInfo(ContentType contentType, ContentType responseContentType, BridgeInfoVO infoVO) {
        if (contentType.getMimeType() == null
                && (responseContentType.getMimeType().equalsIgnoreCase(ContentType.APPLICATION_JSON.getMimeType()))) {
            bridgeService.addBridge(infoVO);
        } else if (contentType.getMimeType() != null
                && (contentType.getMimeType().equalsIgnoreCase(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                || contentType.getMimeType().equalsIgnoreCase(ContentType.APPLICATION_JSON.getMimeType()))) {
            bridgeService.addBridge(infoVO);
        }
    }

    /**
     * 获取请求body的内容
     * @param request
     * @return
     */
    private String getRequestBody(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("POST")) {
            StringBuilder builder = new StringBuilder();

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                reader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }

            return builder.toString();
        }
        return "";
    }

    /**
     * 获取请求body中的内容
     * @param contentType
     * @param body
     * @return
     */
    private String getRequestBody(ContentType contentType, String body) {
        if (contentType == null || contentType.getMimeType() == null) {
            return "";
        } else if (contentType.getMimeType().equalsIgnoreCase(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
            List<Map<String, Object>> formList = new ArrayList<>();
            String[] items = body.split("&");
            for(String item : items) {
                Map<String, Object> itemMap = new HashMap<>();
                formList.add(itemMap);

                String[] info = item.split("=");
                itemMap.put("name", info[0]);
                if (info.length > 1) {
                    itemMap.put("value", info[1]);
                } else {
                    itemMap.put("value", "");
                }
            }
            return JSON.toJSONString(formList);
        } else if (contentType.getMimeType().equalsIgnoreCase(ContentType.APPLICATION_JSON.getMimeType())) {
            return body;
        } else {
            return body;
        }
    }

    /**
     * 获取响应body
     * @param entity
     * @return
     * @throws IOException
     */
    private String getResponseBody(HttpEntity entity) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        String responseBody = buffer.toString();

        return responseBody;
    }

    private BusinessWrapper<HttpResponse> requestForwarding(String method, String url, List<ProtocolHeader> headerList, ContentType contentType, String body) throws IOException {
        org.apache.http.client.fluent.Request request = null;
        if (method.equalsIgnoreCase("GET")) {
            request = org.apache.http.client.fluent.Request.Get(url);
        } else if (method.equalsIgnoreCase("POST")) {
            request = org.apache.http.client.fluent.Request.Post(url);
            request.bodyString(body, contentType);
        } else if (method.equalsIgnoreCase("OPTIONS")) {
            request = org.apache.http.client.fluent.Request.Options(url);
        }

        for(ProtocolHeader header : headerList) {
            if (header.getHeaderKey().equalsIgnoreCase("Content-Length")) {
                continue;
            }

            request.setHeader(header.getHeaderKey(), header.getHeaderValue());
        }

        BusinessWrapper<HttpResponse> wrapper;
        try {
            HttpResponse response = request.execute().returnResponse();
            wrapper = new BusinessWrapper<>(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            wrapper = new BusinessWrapper<>(null, e.getMessage(), ErrorCode.serviceFailure.getCode());
        }

        return wrapper;
    }
}
