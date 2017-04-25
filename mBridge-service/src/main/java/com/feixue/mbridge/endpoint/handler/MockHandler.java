package com.feixue.mbridge.endpoint.handler;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.dao.BodyDao;
import com.feixue.mbridge.dao.ProtocolDao;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.protocol.HttpProtocolDO;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.domain.protocol.ProtocolParam;
import com.feixue.mbridge.domain.report.TestReportDO;
import com.feixue.mbridge.domain.response.MockResponseDO;
import com.feixue.mbridge.domain.report.CheckReport;
import com.feixue.mbridge.domain.server.ServerDO;
import com.feixue.mbridge.endpoint.CheckHandler;
import com.feixue.mbridge.service.TestReportService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.eclipse.jetty.http.HttpFields;
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
 * Created by zxxiao on 15/11/29.
 */
public class MockHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(MockHandler.class);

    /*
    托管系统
     */
    private ServerDO serverDO;

    private ProtocolDao protocolDao;

    private BodyDao bodyDao;

    private TestReportService testReportService;

    public MockHandler(ServerDO serverDO, ProtocolDao protocolDao, BodyDao bodyDao, TestReportService testReportService) {
        this.serverDO = serverDO;
        this.protocolDao = protocolDao;
        this.bodyDao = bodyDao;
        this.testReportService = testReportService;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //非受理端口，不做处理
        if (baseRequest.getServerPort() != serverDO.getServerPort()) {
            return;
        }
        //非托管系统,不进行处理
        if (!baseRequest.getServletPath().equals(serverDO.getSystemCode())) {
            return;
        }

        if (response.isCommitted() || baseRequest.isHandled())
            return;

        String targetStr = target.substring(1, target.length());
        int codeIndex = targetStr.indexOf("/");
        if (codeIndex == -1) {
            return;
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "text/html;charset=UTF-8");

        String systemCode = targetStr.substring(0, codeIndex);
        String urlPath = targetStr.substring(targetStr.indexOf("/"), targetStr.length());
        String method = baseRequest.getMethod();

        String requestContentType = request.getContentType();

        HttpProtocolDO httpProtocolDO = protocolDao.getProtocolInfo(urlPath, systemCode);
        if (httpProtocolDO != null) {
            List<ProtocolHeader> headerList = getRequestHeaderList(httpProtocolDO.getId(), request);

            String requestBody = method.equalsIgnoreCase("get") ? null : getRequestBody(request);

            String requestUrl = baseRequest.getUri().toString();

            List<ProtocolParam> requestParamList = getParams(requestUrl);
            List<ProtocolParam> protocolParamList = JSON.parseArray(httpProtocolDO.getParamList(), ProtocolParam.class);

            Map<String, CheckReport> checkReportMap = new HashMap<>();

            CheckReport headerCheckResult = checkHeader(headerList, JSON.parseArray(httpProtocolDO.getRequestHeader(), ProtocolHeader.class));
            CheckReport paramCheckResult = checkParam(requestParamList, protocolParamList);

            if (method.equalsIgnoreCase("POST")) {
                CheckReport requestBodyCheckResult = CheckHandler.checkResponseBody(requestBody, httpProtocolDO.getRequestBody());
                checkReportMap.put("body", requestBodyCheckResult);
            }

            checkReportMap.put("header", headerCheckResult);
            checkReportMap.put("param", paramCheckResult);

            if (httpProtocolDO.getRequestType().equalsIgnoreCase(method)) {
                if (!paramCheckResult.isStatus()) { //400 error
                    response.setStatus(HttpStatus.SC_BAD_REQUEST);

                    saveTestReport(requestUrl, HttpStatus.SC_BAD_REQUEST, requestContentType, headerList, httpProtocolDO,
                            requestBody, httpProtocolDO.getResponseBody(), checkReportMap, requestParamList);

                    response.getWriter().println(paramCheckResult.toString());
                } else {
                    response.setContentType(ContentType.APPLICATION_JSON.toString());

                    MockResponseDO mockResponseDO = bodyDao.getProtocolNowResponse(httpProtocolDO.getId());
                    if (mockResponseDO == null) {   //无当前响应，则响应指定错误描述信息
                        saveTestReport(requestUrl, HttpStatus.SC_OK, requestContentType, headerList, httpProtocolDO,
                                requestBody, null, checkReportMap, requestParamList);

                        response.getWriter().println(ErrorCode.protocolNoNowResponse.getMsg());
                    } else {    //有当前响应，则响应当前响应内容
                        saveTestReport(requestUrl, HttpStatus.SC_OK, requestContentType, headerList, httpProtocolDO,
                                requestBody, mockResponseDO.getBody(), checkReportMap, requestParamList);

                        response.getWriter().println(mockResponseDO.getBody());
                    }
                }
            } else {    //405 error
                String responseBody = "url :" + urlPath + " need " + httpProtocolDO.getRequestType() + "!";
                saveTestReport(requestUrl,HttpStatus.SC_METHOD_NOT_ALLOWED, requestContentType, headerList, httpProtocolDO,
                        requestBody, responseBody, checkReportMap, requestParamList);

                response.setStatus(HttpStatus.SC_METHOD_NOT_ALLOWED);
                response.getWriter().println(responseBody);
            }

            response.flushBuffer();

            baseRequest.setHandled(true);
        } else {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
            response.getWriter().println("此url暂未收录!");
        }
    }

    /**
     * 检查是否为指定server的处理handler
     * @param checkServerDO
     * @return
     */
    public boolean checkEqual(ServerDO checkServerDO) {
        if (serverDO.getServerPort() == checkServerDO.getServerPort()
                && serverDO.getSystemCode().equals(checkServerDO.getSystemCode())) {
            return true;
        }
        return false;
    }

    /**
     * 获取headers
     * @param protocolId
     * @param request
     * @return
     */
    private List<ProtocolHeader> getRequestHeaderList(long protocolId, HttpServletRequest request) {
        List<ProtocolHeader> headerList = new ArrayList<>();
        HttpFields httpFields = ((Request) request).getHttpFields();
        for(String fieldName : httpFields.getFieldNamesCollection()) {
            headerList.add(new ProtocolHeader(protocolId, fieldName, httpFields.getStringField(fieldName)));
        }

        return headerList;
    }

    /**
     * 检查参数是否满足要求
     * @param requestParams
     * @param mockParams
     * @return
     */
    private CheckReport checkParam(List<ProtocolParam> requestParams, List<ProtocolParam> mockParams) {
        if (mockParams.isEmpty()) {
            return new CheckReport(true);
        }

        boolean checkResult = true;
        List<String> warnInfo = new ArrayList<>();
        for(ProtocolParam mockParam : mockParams) {
            boolean hasParam = false;
            for(ProtocolParam protocolParam : requestParams) {
                if (mockParam.getParamName().equals(protocolParam.getParamName())) {
                    hasParam = true;
                    String tmp = "参数" + mockParam.getParamName() + "存在，";
                    if (!mockParam.isRequired()
                            || (mockParam.isRequired() && !StringUtils.isEmpty(protocolParam.getParamValue().toString()))) {
                    } else {
                        tmp += "未赋值！";

                        warnInfo.add(tmp);
                    }
                    break;
                }
            }
            if (!hasParam) {
                checkResult = false;
                warnInfo.add("参数" + mockParam.getParamName() + "不存在！");
            }
        }

        return new CheckReport(checkResult, warnInfo);
    }

    /**
     * header校验
     * @param requestHeaders
     * @param mockHeaders
     * @return
     */
    private CheckReport checkHeader(List<ProtocolHeader> requestHeaders, List<ProtocolHeader> mockHeaders) {
        if (mockHeaders == null || mockHeaders.isEmpty()) {
            return new CheckReport(true);
        }

        List<String> warnInfo = new ArrayList<>();
        for(ProtocolHeader protocolHeader : mockHeaders) {
            boolean hasHeader = false;
            for(ProtocolHeader header : requestHeaders) {
                if (header.getHeaderKey().equalsIgnoreCase(protocolHeader.getHeaderKey())) {
                    hasHeader = true;
                    break;
                }
            }
            if (!hasHeader) {
                warnInfo.add("header缺失:" + protocolHeader.getHeaderKey());
            }
        }
        if (warnInfo.isEmpty()) {
            return new CheckReport(true);
        } else {
            return new CheckReport(false, warnInfo);
        }
    }

    /**
     * 获取参数集合
     * @param url
     * @return
     */
    private List<ProtocolParam> getParams(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return Collections.emptyList();
        } else {
            String[] params = url.substring(index + 1).split("&");

            List<ProtocolParam> paramList = new ArrayList<>();
            for(String param : params) {
                if (StringUtils.isEmpty(param)) {
                    continue;
                }
                String[] paramInfo = param.split("=");
                ProtocolParam protocolParam;
                if (paramInfo.length == 1) {
                    protocolParam = new ProtocolParam(paramInfo[0], "");
                } else {
                    protocolParam = new ProtocolParam(paramInfo[0], paramInfo[1]);
                }

                paramList.add(protocolParam);
            }

            return paramList;
        }
    }

    private void saveTestReport(String url, int statusCode, String requestContentType, List<ProtocolHeader> headerList, HttpProtocolDO protocol,
                                String requestBody, String responseBody, Map<String, CheckReport> checkReportMap,
                                List<ProtocolParam> requestParamList) {
        TestReportDO testReportDO = new TestReportDO();

        testReportDO.setProtocolId(protocol.getId());
        testReportDO.setSystemCode(protocol.getSystemCode());

        testReportDO.setStatusCode(statusCode);

        testReportDO.setRequestInfo(url);
        testReportDO.setRequestHeader(JSON.toJSONString(headerList));
        testReportDO.setRequestContentType(requestContentType);
        testReportDO.setRequestParam(JSON.toJSONString(requestParamList));
        testReportDO.setRequestBody(requestBody);

        testReportDO.setMockRequestInfo(getMockRequestUrl(protocol));
        testReportDO.setMockRequestHeader(protocol.getRequestHeader());
        testReportDO.setMockRequestParam(protocol.getParamList());
        testReportDO.setMockRequestBody(protocol.getRequestBody());

        testReportDO.setMockResponseHeader(protocol.getResponseHeader());
        testReportDO.setMockResponseBody(responseBody);

        testReportDO.setResponseBody(responseBody);

        testReportDO.setCheckReport(JSON.toJSONString(checkReportMap));

        testReportDO.setTestType(TestReportDO.TestType.client.getType());

        boolean testResult = statusCode == 200 ? true : false;
        for(CheckReport checkReport : checkReportMap.values()) {
            testResult &= checkReport.isStatus();
        }
        testReportDO.setTestResult(testResult == true ? 0 : 1);

        testReportService.addTestReport(testReportDO);
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
     * 获取模拟请求url
     * @param protocolPO
     * @return
     */
    private String getMockRequestUrl(HttpProtocolDO protocolPO) {
        String systemCode = protocolPO.getSystemCode();
        String baseUrl = protocolPO.getUrlPath();
        List<ProtocolParam> paramList = JSON.parseArray(protocolPO.getParamList(), ProtocolParam.class);
        if (!paramList.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("/");
            buffer.append(systemCode);
            buffer.append(baseUrl);

            buffer.append("?");
            for(ProtocolParam param : paramList) {
                buffer.append(param.getParamName());
                buffer.append("=");
                buffer.append(param.getParamValue());
                buffer.append("&");
            }
            String resultUrl = buffer.toString();
            return resultUrl.substring(0, resultUrl.length() - 1);
        }
        return baseUrl;
    }
}
