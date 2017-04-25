package com.feixue.mbridge.resolver;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zxxiao on 16/6/18.
 */
public class JSONExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(JSONExceptionResolver.class);

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("异常：" + ex.getMessage(), ex);

        HttpResponse result = new HttpResponse(ErrorCode.serviceFailure);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {
            response.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            logger.error("通讯异常:"+ e.getMessage(), e);
        }

        return new ModelAndView();
    }
}