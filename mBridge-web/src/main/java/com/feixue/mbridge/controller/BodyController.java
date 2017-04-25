package com.feixue.mbridge.controller;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.HttpResponse;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.request.MockRequestVO;
import com.feixue.mbridge.domain.response.MockResponseDO;
import com.feixue.mbridge.domain.response.MockResponseVO;
import com.feixue.mbridge.service.BodyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 16/5/12.
 */
@Controller
public class BodyController {

    private static final Logger logger = LoggerFactory.getLogger(BodyController.class);

    @Resource
    private BodyService bodyService;

    @RESTfulDoc("添加请求mock")
    @RequestMapping(value = "/request/mock/add", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse addMockRequest(@RequestBody MockRequestVO mockRequestVO) {
        return new HttpResponse(bodyService.addMockRequest(mockRequestVO));
    }

    @RESTfulDoc("查询指定协议的请求mock集合")
    @RequestMapping(value = "/request/mock/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse getMockRequest(@RequestParam long protocolId, @RequestParam int pageStart, @RequestParam int pageLength) {
        return new HttpResponse(bodyService.getRequestMockByProtocolIdPage(protocolId, pageStart * pageLength, pageLength));
    }

    @RESTfulDoc("删除指定id的请求mock")
    @RequestMapping(value = "/request/mock/del", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse delMockRequest(@RequestParam long id) {
        return new HttpResponse(bodyService.delRequestMockById(id));
    }

    @RESTfulDoc("获取指定协议的mock响应列表")
    @RequestMapping(value = "/response/mock/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse<TablePageVO<List<MockResponseDO>>> getMockResponses(
            @RequestParam long protocolId, @RequestParam int pageStart, @RequestParam int pageLength) {
        TablePageVO<List<MockResponseVO>> tablePageVO = bodyService.getMockResponsePage(protocolId, pageStart, pageLength);

        BusinessWrapper<MockResponseVO> wrapper = bodyService.getProtocolNowResponse(protocolId);
        Map<String, Object> resultMap = new HashMap<>();
        if (wrapper.isSuccess()) {
            resultMap.put("nowResponse", wrapper.getBody());
        } else {
            resultMap.put("nowResponse", null);
        }
        resultMap.put("tableData", tablePageVO);
        return new HttpResponse(resultMap);
    }

    @RESTfulDoc("保存mock response")
    @RequestMapping(value = "/response/mock/add", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse saveMockResponse(@RequestBody MockResponseVO mockResponseVO) {
        return new HttpResponse(bodyService.addMockResponse(mockResponseVO));
    }

    @RESTfulDoc("删除指定编号的mock response body")
    @RequestMapping(value = "/mock/responseBody/del", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse delMockResponseBody(@RequestParam long id) {
        return new HttpResponse(bodyService.delMockResponse(id));
    }

    @RESTfulDoc("设置模拟响应为当前响应")
    @RequestMapping(value = "/mock/response/setNow", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse setMockResponseNow(@RequestParam long id, @RequestParam long protocolId) {
        return new HttpResponse(bodyService.setMockResponseNow(id, protocolId));
    }
}
