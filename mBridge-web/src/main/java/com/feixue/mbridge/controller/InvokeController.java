package com.feixue.mbridge.controller;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.HttpResponse;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.domain.report.TestReportVO;
import com.feixue.mbridge.domain.request.MockRequestVO;
import com.feixue.mbridge.endpoint.ClientProxy;
import com.feixue.mbridge.service.BodyService;
import com.feixue.mbridge.service.MBridgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by zxxiao on 16/5/16.
 */
@Controller
@RequestMapping("/invoke")
public class InvokeController {

    private static final Logger logger = LoggerFactory.getLogger(InvokeController.class);

    @Resource
    private ClientProxy clientProxy;

    @Resource
    private BodyService bodyService;

    @Resource
    private MBridgeService mBridgeService;

    @RESTfulDoc("执行测试")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse doInvokeTest(@RequestParam long protocolId, @RequestParam long mockId) {
        HttpProtocolVO protocolVO = mBridgeService.getProtocolDetail(protocolId);
        MockRequestVO mockRequestVO = bodyService.getRequestMockById(mockId);

        try {
            TestReportVO testResult = clientProxy.remoteTest(protocolVO, mockRequestVO);

            return new HttpResponse(testResult);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return new HttpResponse(ErrorCode.serviceFailure.getCode(), e.getMessage());
        }
    }
}
