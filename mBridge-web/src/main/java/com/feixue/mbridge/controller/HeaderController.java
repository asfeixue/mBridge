package com.feixue.mbridge.controller;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.HttpResponse;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.service.HeaderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zxxiao on 16/5/31.
 */
@Controller
@RequestMapping("/header")
public class HeaderController {

    @Resource
    private HeaderService headerService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @RESTfulDoc("保存header数据")
    @ResponseBody
    public HttpResponse<Boolean> saveGroupHeaders(@RequestParam int headerType, @RequestParam int index,
                                                  @RequestParam long protocolId,
                                                  @RequestBody List<ProtocolHeader> protocolHeaderList,
                                                  @RequestParam long headerId) {
        headerService.saveHeader(headerType, index, protocolId, protocolHeaderList, headerId);
        return new HttpResponse(true);
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    @RESTfulDoc("获取指定group的header")
    public HttpResponse<Map<String, Object>> getTypeHeaders(@RequestParam long protocolId) {
        return new HttpResponse(headerService.getTypeHeaders(protocolId));
    }
}
