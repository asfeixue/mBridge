package com.feixue.mbridge.endpoint;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.domain.protocol.ProtocolParam;
import com.feixue.mbridge.domain.protocol.ProtocolPath;
import com.feixue.mbridge.domain.report.CheckReport;
import com.feixue.mbridge.domain.report.TestReportDO;
import com.feixue.mbridge.domain.report.TestReportVO;
import com.feixue.mbridge.domain.request.MockRequestVO;
import com.feixue.mbridge.service.TestReportService;
import com.feixue.mbridge.util.job.BaseJob;
import com.feixue.mbridge.util.job.SchedulerManager;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户端代理，定时&动态调用后端API，执行测试
 * Created by zxxiao on 15/11/8.
 */
@Service
public class ClientProxy implements BaseJob, InitializingBean {

    @Resource
    private TestReportService testReportService;

    @Autowired
    private SchedulerManager schedulerManager;

    private HttpClient httpClient;

    /**
     * 用指定的client mock，测试指定的协议
     * @param protocol
     * @param mockRequestVO
     */
    public TestReportVO remoteTest(HttpProtocolVO protocol, MockRequestVO mockRequestVO) throws IOException {
        BusinessWrapper<Boolean> wrapper = baseCheck(protocol, mockRequestVO);
        if (!wrapper.isSuccess()) {
            TestReportDO testReportDO = new TestReportDO();
            testReportDO.setTestResult(TestReportDO.TestResult.failure.getCode());
            testReportDO.setProtocolId(protocol.getId());
            testReportDO.setSystemCode(protocol.getSystem().getSystemCode());
            testReportDO.setStatusCode(-1);
            testReportDO.setRequestInfo("");
            testReportDO.setRequestHeader(JSON.toJSONString(mockRequestVO.getRequestHeader()));
            testReportDO.setRequestParam(JSON.toJSONString(mockRequestVO.getParamList()));
            testReportDO.setRequestBody(mockRequestVO.getRequestBody());

            testReportDO.setResponseHeader(null);
            testReportDO.setResponseBody(null);

            testReportDO.setMockRequestInfo(protocol.getRequestUrl());
            testReportDO.setMockRequestHeader(JSON.toJSONString(protocol.getRequestHeader()));
            testReportDO.setMockRequestParam(JSON.toJSONString(protocol.getParamList()));
            testReportDO.setMockRequestBody(JSON.toJSONString(protocol.getRequestBody()));
            testReportDO.setMockResponseHeader(JSON.toJSONString(protocol.getResponseHeader()));
            testReportDO.setMockResponseBody(JSON.toJSONString(protocol.getResponseBody()));

            testReportDO.setCheckReport(null);
            testReportDO.setTestType(TestReportDO.TestType.server.getType());

            testReportService.addTestReport(testReportDO);

            return new TestReportVO(testReportDO, protocol.getSystem());
        }

        String requestUrl = getRequestUrl(protocol, mockRequestVO);

        HttpResponse response = null;

        if (protocol.getResponseBody() == null && protocol.getRequestBody() == null) {
            HttpOptions httpOptions = new HttpOptions(requestUrl);
            addHeaderItems(mockRequestVO, httpOptions);

            response = httpClient.execute(httpOptions);
        }else if (protocol.getRequestType().equalsIgnoreCase("get")) {
            HttpGet httpGet = new HttpGet(requestUrl);
            addHeaderItems(mockRequestVO, httpGet);

            response = httpClient.execute(httpGet);
        } else if (protocol.getRequestType().equalsIgnoreCase("post")) {
            HttpPost httpPost = new HttpPost(requestUrl);
            addHeaderItems(mockRequestVO, httpPost);

            response = executePostTest(httpPost, protocol, mockRequestVO);
        }

        TestReportDO testReportDO = checkResponseAndBuildReport(requestUrl, protocol, response, mockRequestVO);

        testReportService.addTestReport(testReportDO);

        return new TestReportVO(testReportDO, protocol.getSystem());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        schedulerManager.registerJob(this);

        httpClient = HttpClientBuilder.create().setRedirectStrategy(new RedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return false;
            }

            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return null;
            }
        }).build();
    }

    @Override
    public void execute() {
//        for(final MockRequestVO mockRequestVO : protocolDao.getHasPlanMockJob()) {
//            final ProtocolServer server = protocolDao.getRemoteInfo(mockRequestVO.getSystemCode(), mockRequestVO.getRemoteEnv());
//
//            HttpProtocolDO protocol = protocolDao.getProtocol(mockRequestVO.getProtocolId());
//            final HttpProtocolVO protocolDO = new HttpProtocolVO(protocol);
//
//            schedulerManager.registerJob(new BaseJob() {
//                @Override
//                public void execute() {
//                    try {
//                        ClientProxy.this.remoteTest(server, protocolDO , mockRequestVO);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, mockRequestVO.getSchedulePlan(), mockRequestVO.getProtocolId() + ":" + mockRequestVO.getId());
//        }
    }

    /**
     * 检查param是否满足要求
     * @param protocol
     * @param mockRequestVO
     * @return
     */
    private CheckReport checkParam(HttpProtocolVO protocol, MockRequestVO mockRequestVO) {
        CheckReport checkReport = new CheckReport(true);
        List<String> reportList = new ArrayList<>();
        checkReport.setCheckDesc(reportList);

        for(ProtocolParam protocolParam : protocol.getParamList()) {
            if (protocolParam.isRequired()) {
                boolean hasParam = false;
                for(ProtocolParam mockParam : mockRequestVO.getParamList()) {
                    if (mockParam.getParamName().equals(protocolParam.getParamName())) {
                        hasParam = true;
                    }
                }
                if (!hasParam) {
                    reportList.add("参数:" + protocolParam.getParamName() + "未提供！");
                }
            }
        }

        if (!reportList.isEmpty()) {
            checkReport.setStatus(false);
        }

        return checkReport;
    }

    /**
     * 如果发生跳转，则将redirect url记录
     * @param response
     * @param testReportDO
     */
    private void setRedirect(HttpResponse response, TestReportDO testReportDO) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY || statusCode == HttpStatus.SC_MOVED_PERMANENTLY) {
            Header[] headers = response.getHeaders("Location");
            if(headers!=null && headers.length>0){
                String redirectUrl = headers[0].getValue();
                redirectUrl = redirectUrl.replace(" ", "%20");

                testReportDO.setRedirectUrl(redirectUrl);
            }
        }
    }

    /**
     * 检查response并生成测试报告
     * @param requestUrl
     * @param protocol
     * @param response
     * @param mockRequestVO
     * @return
     * @throws IOException
     */
    private TestReportDO checkResponseAndBuildReport(String requestUrl, HttpProtocolVO protocol, HttpResponse response, MockRequestVO mockRequestVO) throws IOException {
//        ByteArrayEntity entity = (ByteArrayEntity) response.getEntity();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
//        StringBuffer buffer = new StringBuffer();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            buffer.append(line);
//        }
//
//        String responseBody = buffer.toString();

        String responseBody = EntityUtils.toString(response.getEntity());

        List<ProtocolHeader> header = getHeaders(mockRequestVO.getProtocolId(), response.getAllHeaders());

        CheckReport headerCheckResult = CheckHandler.checkHeader(header, protocol.getResponseHeader());
        CheckReport paramCheckReport = checkParam(protocol, mockRequestVO);
        CheckReport bodyCheckReport = CheckHandler.checkResponseBody(responseBody, protocol.getResponseBody());

        Map<String, CheckReport> checkReportMap = new HashMap<>();
        checkReportMap.put("header", headerCheckResult);
        checkReportMap.put("param", paramCheckReport);
        checkReportMap.put("body", bodyCheckReport);

        int statusCode = response.getStatusLine().getStatusCode();
        TestReportDO testReportDO = new TestReportDO();

        setRedirect(response, testReportDO);

        testReportDO.setProtocolId(mockRequestVO.getProtocolId());
        testReportDO.setSystemCode(protocol.getSystem().getSystemCode());
        testReportDO.setStatusCode(statusCode);
        testReportDO.setRequestInfo(requestUrl);
        testReportDO.setRequestHeader(JSON.toJSONString(mockRequestVO.getRequestHeader()));
        testReportDO.setRequestParam(JSON.toJSONString(mockRequestVO.getParamList()));
        testReportDO.setRequestBody(mockRequestVO.getRequestBody());

        testReportDO.setResponseHeader(JSON.toJSONString(header));
        testReportDO.setResponseBody(responseBody);

        testReportDO.setMockRequestInfo(protocol.getRequestUrl());
        testReportDO.setMockRequestHeader(JSON.toJSONString(protocol.getRequestHeader()));
        testReportDO.setMockRequestParam(JSON.toJSONString(protocol.getParamList()));
        testReportDO.setMockRequestBody(JSON.toJSONString(protocol.getRequestBody()));
        testReportDO.setMockResponseHeader(JSON.toJSONString(protocol.getResponseHeader()));
        testReportDO.setMockResponseBody(JSON.toJSONString(protocol.getResponseBody()));

        testReportDO.setCheckReport(JSON.toJSONString(checkReportMap));
        testReportDO.setTestType(TestReportDO.TestType.server.getType());

        if (headerCheckResult.isStatus() && paramCheckReport.isStatus() && bodyCheckReport.isStatus()) {
            testReportDO.setTestResult(TestReportDO.TestResult.success.getCode());
        } else {
            testReportDO.setTestResult(TestReportDO.TestResult.failure.getCode());
        }

        return testReportDO;
    }

    /**
     * 执行post测试
     * @param httpPost
     * @param protocol
     * @param mockRequestVO
     * @return
     * @throws IOException
     */
    private HttpResponse executePostTest(HttpPost httpPost, HttpProtocolVO protocol, MockRequestVO mockRequestVO) throws IOException {
        HttpResponse response;

        if (protocol.getContentType().equalsIgnoreCase(HttpProtocolVO.ProtocolContentType.form)) {
            HttpEntity httpEntity = new UrlEncodedFormEntity(mockFrom(mockRequestVO.getRequestBody()));
            httpPost.setEntity(httpEntity);
        } else if (protocol.getContentType().equalsIgnoreCase(HttpProtocolVO.ProtocolContentType.json)) {
            String requestBody = mockRequestVO.getRequestBody() == null ? "" : mockRequestVO.getRequestBody();
            StringEntity stringEntity = new StringEntity(requestBody);
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType(ContentType.APPLICATION_JSON.toString());
            httpPost.setEntity(stringEntity);
        }
        response = httpClient.execute(httpPost);
        return response;
    }

    /**
     * 针对表单请求结构进行mock
     * @param from
     * @return
     */
    private List<NameValuePair> mockFrom(String from) {
        List<NameValuePair> pairList = new ArrayList<>();
        Map<String, Object> fromMap = JSON.parseObject(from, Map.class);

        for(Map.Entry<String, Object> entry : fromMap.entrySet()) {
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
            pairList.add(pair);
        }

        return pairList;
    }

    /**
     * 获取请求URL
     * @param protocol
     * @param mockRequestVO
     * @return
     */
    private String getRequestUrl(HttpProtocolVO protocol, MockRequestVO mockRequestVO) {
        String url = protocol.getUrlPath();

        url = mockParam(url, protocol, mockRequestVO);

        return mockRequestVO.getSupportEnv().getEnvAddress() + "/" + protocol.getSystem().getSystemCode() + url;
    }

    /**
     * 针对参数进行mock
     * @param url
     * @param protocol
     * @param mockRequestVO
     */
    private String mockParam(String url, HttpProtocolVO protocol, MockRequestVO mockRequestVO) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(url);
        if (protocol.getParamList() != null && !protocol.getParamList().isEmpty()) {
            buffer.append("?");
            for(ProtocolParam protocolParam : mockRequestVO.getParamList()) {
                buffer.append(protocolParam.getParamName());
                buffer.append("=");
                buffer.append(protocolParam.getParamValue());
                buffer.append("&");
            }
            url = buffer.toString();
            if (url.endsWith("&")) {
                url = url.substring(0, url.length() - 1);
            }
        }
        return url;
    }

    /**
     * 路径排序
     * @param paths
     */
    private void pathSort(List<ProtocolPath> paths) {
        for(int left = 0; left < paths.size(); left++) {
            for(int right = left + 1; right < paths.size(); right++) {
                if (paths.get(left).getIndex() > paths.get(right).getIndex()) {
                    ProtocolPath temp = paths.get(left);
                    paths.set(left, paths.get(right));
                    paths.set(right, temp);
                }
            }
        }
    }

    /**
     * 获取所有的header
     * @param headers
     * @return
     */
    private List<ProtocolHeader> getHeaders(long protocolId, Header[] headers) {
        List<ProtocolHeader> protocolHeaderList = new ArrayList<>();
        for(Header header : headers) {
            ProtocolHeader protocolHeader = new ProtocolHeader(protocolId, header.getName(), header.getValue());
            protocolHeaderList.add(protocolHeader);
        }

        return protocolHeaderList;
    }

    /**
     * 将mock需要的header加入请求中
     * @param mockRequestVO
     * @param request
     */
    private void addHeaderItems(MockRequestVO mockRequestVO, Request request) {
        List<ProtocolHeader> headerList;
        if (mockRequestVO.getRequestHeaderSubscribe() != null) {
            headerList = mockRequestVO.getRequestHeaderSubscribe().getTplValue();
        } else {
            headerList = mockRequestVO.getRequestHeader();
        }

        if (headerList == null) {
            return;
        }

        for(ProtocolHeader protocolHeader : headerList) {
            request.addHeader(protocolHeader.getHeaderKey(), protocolHeader.getHeaderValue());
        }
    }

    /**
     * 将mock需要的header加入请求中
     * @param mockRequestVO
     * @param httpRequest
     */
    private void addHeaderItems(MockRequestVO mockRequestVO, HttpRequestBase httpRequest) {
        List<ProtocolHeader> headerList;
        if (mockRequestVO.getRequestHeaderSubscribe() != null) {
            headerList = mockRequestVO.getRequestHeaderSubscribe().getTplValue();
        } else {
            headerList = mockRequestVO.getRequestHeader();
        }

        if (headerList == null) {
            return;
        }

        for(ProtocolHeader protocolHeader : headerList) {
            httpRequest.addHeader(protocolHeader.getHeaderKey(), protocolHeader.getHeaderValue());
        }
    }

    /**
     * 基础校验
     * @param protocol
     * @param mockRequestVO
     * @return
     */
    private BusinessWrapper<Boolean> baseCheck(HttpProtocolVO protocol, MockRequestVO mockRequestVO) {
        if (protocol.getParamList() == null && mockRequestVO.getParamList() == null) {
            return new BusinessWrapper<>(true);
        } else if (protocol.getParamList() != null && protocol.getParamList().isEmpty()
                && mockRequestVO.getParamList() != null && mockRequestVO.getParamList().isEmpty()) {
            return new BusinessWrapper<>(true);
        } else if (protocol.getParamList().size() == mockRequestVO.getParamList().size()) {
            return new BusinessWrapper<>(true);
        } else {
            for(ProtocolParam param : protocol.getParamList()) {
                if (param.isRequired()) {
                    boolean hasParam = false;
                    for(ProtocolParam mockParam : mockRequestVO.getParamList()) {
                        if (param.getParamName().equals(mockParam.getParamName())) {
                            hasParam = true;
                            break;
                        }
                    }
                    if (!hasParam) {
                        return new BusinessWrapper<>(ErrorCode.paramNotMatch);
                    }
                }
            }
            return new BusinessWrapper<>(true);
        }
    }
}
