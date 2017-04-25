package com.feixue.mbridge.controller;

import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.HttpResponse;
import com.feixue.mbridge.domain.TablePageVO;
import com.feixue.mbridge.domain.tpl.HeaderTplDO;
import com.feixue.mbridge.domain.tpl.HeaderTplVO;
import com.feixue.mbridge.service.HeaderTplService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zxxiao on 16/5/7.
 */
@Controller
public class TplController {

    @Resource
    private HeaderTplService headerTplService;

    @RESTfulDoc("添加header模板")
    @RequestMapping(value = "/tpl/header/add", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse addHeaderTpl(@RequestBody HeaderTplDO headerTplDO) {
        return new HttpResponse(headerTplService.addHeaderTpl(headerTplDO));
    }

    @RESTfulDoc("查询header模板")
    @RequestMapping(value = "/tpl/header/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResponse getHeaderTpl(@RequestParam int pageStart, @RequestParam int pageLength) {
        TablePageVO<List<HeaderTplVO>> tablePageVO = headerTplService.getHeaderTplPage(pageStart, pageLength);

        return new HttpResponse(tablePageVO);
    }

    @RESTfulDoc("删除header模板")
    @RequestMapping(value = "/tpl/header/del", method = RequestMethod.POST)
    @ResponseBody
    public HttpResponse delHeaderTpl(@RequestBody HeaderTplDO headerTplDO) {
        return new HttpResponse(headerTplService.delTpl(headerTplDO));
    }
}
