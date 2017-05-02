package com.feixue.mbridge.controller;

import com.feixue.mbridge.domain.system.SystemVO;
import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.*;
import com.feixue.mbridge.domain.system.SystemEnvDO;
import com.feixue.mbridge.domain.system.SystemDO;
import com.feixue.mbridge.domain.system.SystemEnvVO;
import com.feixue.mbridge.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 16/5/16.
 */
@Controller
@RequestMapping("/system")
public class SystemController {

    @Resource
    private SystemService systemService;

    @RESTfulDoc("保存 or 更新系统")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse saveSystem(@RequestBody SystemVO systemVO) {
        BusinessWrapper<Boolean> wrapper = systemService.saveSystem(systemVO);
        if (wrapper.isSuccess()) {
            return new HttpResponse(wrapper.getBody());
        } else {
            return new HttpResponse(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RESTfulDoc("系统删除")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResponse delSystem(@RequestParam String systemCode) {
        BusinessWrapper<Boolean> wrapper = systemService.delSystem(systemCode);
        if (wrapper.isSuccess()) {
            return new HttpResponse(wrapper.getBody());
        } else {
            return new HttpResponse(wrapper.getCode(), wrapper.getMsg());
        }
    }

    @RESTfulDoc("获取系统列表")
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse<TablePageVO<List<SystemVO>>> getSystems(
            @RequestParam String systemCode, @RequestParam int page, @RequestParam int length) {
        return new HttpResponse<>(systemService.getSystemPage(systemCode, page, length));
    }

    @RESTfulDoc("获取所有系统")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse<List<SystemDO>> getSystemList() {
        return new HttpResponse<>(systemService.getSystemList());
    }
}
