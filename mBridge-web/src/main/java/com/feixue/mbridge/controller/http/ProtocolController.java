package com.feixue.mbridge.controller.http;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.HttpResponse;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.service.MBridgeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 15/10/9.
 */
@Controller
public class ProtocolController {

    @Resource
    private MBridgeService mBridgeService;

    @RequestMapping(value = "/dashboard")
    public String hello() {
        return "index";
    }

    @RESTfulDoc("获取所有协议集合")
    @RequestMapping(value = "/protocols", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse<TablePageVO<List<HttpProtocolVO>>> getProtocols(
            @RequestParam String url, @RequestParam(required = false) String systemCode, @RequestParam int page, @RequestParam int length) {
        return new HttpResponse(mBridgeService.getProtocols(url, systemCode, page * length, length));
    }

    @RESTfulDoc("查询指定的协议详情")
    @RequestMapping(value = "/protocol/detail", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse<HttpProtocolVO> getProtocolDetail(@RequestParam long id) {
        HttpProtocolVO protocolVO = mBridgeService.getProtocolDetail(id);
        if (protocolVO == null) {
            return new HttpResponse<>(ErrorCode.protocolNotExist);
        }
        return new HttpResponse<>(protocolVO);
    }

    @RESTfulDoc("删除指定的协议详情")
    @RequestMapping(value = "/protocol/del", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse delProtocol(@RequestParam long id) {
        return new HttpResponse(mBridgeService.delProtocol(id));
    }

    @RESTfulDoc("保存指定的协议详情")
    @RequestMapping(value = "/protocol/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse addProtocol(@RequestBody HttpProtocolVO protocolVO) {
        return new HttpResponse(mBridgeService.saveProtocol(protocolVO));
    }
}
