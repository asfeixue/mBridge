package com.feixue.mbridge.controller;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.HttpResponse;
import com.feixue.mbridge.domain.server.ServerVO;
import com.feixue.mbridge.service.ServerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 16/9/30.
 */
@Controller
@RequestMapping("/server")
public class ServerController {

    @Resource
    private ServerService serverService;

    @RESTfulDoc("获取指定的mock server列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse queryServer(@RequestParam String systemCode, @RequestParam int page, @RequestParam int length) {
        return new HttpResponse(serverService.queryServer(systemCode, page, length));
    }

    @RESTfulDoc("新增mock server")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse addServer(@RequestBody ServerVO serverVO) {
        BusinessWrapper<Boolean> wrapper = serverService.addServer(serverVO);
        if (wrapper.isSuccess()) {
            return new HttpResponse(wrapper.getBody());
        } else {
            return new HttpResponse(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RESTfulDoc("删除指定的mock server")
    @RequestMapping(value = "/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResponse delServer(@RequestParam long serverId) {
        BusinessWrapper<Boolean> wrapper = serverService.delServer(serverId);
        if (wrapper.isSuccess()) {
            return new HttpResponse(wrapper.getBody());
        } else {
            return new HttpResponse(wrapper.getCode(), wrapper.getMsg());
        }
    }
}
