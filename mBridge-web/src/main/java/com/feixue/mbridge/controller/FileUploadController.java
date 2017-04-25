package com.feixue.mbridge.controller;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.meta.annotation.RESTfulDoc;
import com.feixue.mbridge.domain.protocol.HttpProtocolVO;
import com.feixue.mbridge.meta.domain.HttpProtocol;
import com.feixue.mbridge.service.MBridgeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Controller
@RequestMapping(value = "/upload")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private MBridgeService mBridgeService;

    @RESTfulDoc("加载协议文档")
    @RequestMapping(value = "/protocols", method = RequestMethod.POST)
    @ResponseBody
    public void loadProtocolFile(@RequestParam("file") MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        String line = reader.readLine();
        while (StringUtils.isNotEmpty(line)) {
            buffer.append(line);
            line = reader.readLine();
        }

        try {
            List<HttpProtocol> list = JSON.parseArray(buffer.toString(), HttpProtocol.class);
            mBridgeService.collect(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
