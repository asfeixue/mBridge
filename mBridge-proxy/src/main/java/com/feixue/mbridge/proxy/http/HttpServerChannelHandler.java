package com.feixue.mbridge.proxy.http;

import com.feixue.mbridge.domain.BusinessWrapper;
import com.feixue.mbridge.domain.ErrorCode;
import com.feixue.mbridge.domain.proxy.HttpProxyContent;
import com.feixue.mbridge.proxy.CallbackNotifyChain;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by zxxiao on 2017/7/12.
 */
public class HttpServerChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
    private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
    private static final AsciiString CONNECTION = new AsciiString("Connection");
    private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
    private static final AsciiString DATE = new AsciiString("Date");

    private static final Logger logger = LoggerFactory.getLogger(HttpServerChannelHandler.class);

    private CallbackNotifyChain<HttpProxyContent, BusinessWrapper> notifyChain;

    public HttpServerChannelHandler(CallbackNotifyChain<HttpProxyContent, BusinessWrapper> notifyChain) {
        this.notifyChain = notifyChain;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        boolean keepAlive = HttpUtil.isKeepAlive(msg);
        try {
            HttpProxyContent proxyContent = new HttpProxyContent();
            proxyContent.setUrl(msg.uri());
            proxyContent.setMethod(msg.method().name());
            Map<String, String> headersMap = buildHeader(msg);
            proxyContent.setHeadersMap(headersMap);
            proxyContent.setContentType(headersMap.get(CONTENT_TYPE));
            buildBody(msg, proxyContent);

            BusinessWrapper wrapper = new BusinessWrapper();

            notifyChain.doInvoke(proxyContent, wrapper);

            FullHttpResponse response;
            if (wrapper.isSuccess()) {
                HttpProxyContent responseContent = (HttpProxyContent) wrapper.getBody();
                Object responseBody = responseContent.getBody();
                String responseBodyStr = responseBody == null ? "未指定任何响应" : responseBody.toString();
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(responseBodyStr.getBytes()));

                response.headers().set(CONTENT_TYPE, responseContent.getContentType());
            } else {
                if (wrapper.getCode().equals(ErrorCode.protocolNotExist.getCode())) {
                    response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, Unpooled.wrappedBuffer(wrapper.getMsg().getBytes()));
                } else {
                    response = new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer(wrapper.getMsg().getBytes()));
                }
            }

            response.headers().set(DATE, new Date());
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

            if (!keepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(CONNECTION, KEEP_ALIVE);
                ctx.write(response);
            }
        } catch (Exception e) {
            internalServerError(keepAlive, ctx, "mBridge process error with:" + e.getMessage());
            throw e;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }

    /**
     * 处理body
     * @param request
     * @param proxyContent
     */
    private void buildBody(FullHttpRequest request, HttpProxyContent proxyContent) {
        if (request.method().name().equals(HttpMethod.POST.name())) {
            int contentLength = request.headers().getInt(CONTENT_LENGTH);
            byte[] bodyBytes = new byte[contentLength];
            request.content().readBytes(bodyBytes);

            //TODO 编码问题后续需要处理
            String requestBody = new String(bodyBytes);
            proxyContent.setBody(requestBody);
        }
    }

    /**
     * 转换header
     * @param request
     * @return
     */
    private Map<String, String> buildHeader(FullHttpRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        for(Map.Entry<String, String> entry : request.headers().entries()) {
            headerMap.put(entry.getKey(), entry.getValue());
        }

        return headerMap;
    }

    /**
     * 内部处理异常
     * @param keepAlive
     * @param ctx
     * @param msg
     */
    private void internalServerError(boolean keepAlive, ChannelHandlerContext ctx, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer(msg.getBytes()));

        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());

        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(response);
        }
    }
}
