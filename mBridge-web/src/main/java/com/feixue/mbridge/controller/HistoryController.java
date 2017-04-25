package com.feixue.mbridge.controller;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.HttpResponse;
import com.feixue.mbridge.service.TestReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 16/5/1.
 */
@Controller
public class HistoryController {

    @Resource
    private TestReportService testReportService;

    @RESTfulDoc("客户端请求历史")
    @RequestMapping(value = "/history/client", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse getClientHistory(@RequestParam long protocolId, @RequestParam int page,
                                         @RequestParam int length) {
        return new HttpResponse(testReportService.getClientHistoryPage(protocolId, page * length, length));
    }

    @RESTfulDoc("服务端请求历史")
    @RequestMapping(value = "/history/server", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse getServerHistory(@RequestParam long protocolId, @RequestParam int page,
                                         @RequestParam int length) {
        return new HttpResponse(testReportService.getServerHistoryPage(protocolId, page * length, length));
    }
}
