package com.feixue.mbridge.proxy.http.filter;

import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import com.feixue.mbridge.proxy.*;
import com.feixue.mbridge.proxy.http.HttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AsciiString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransformFilter extends AbstractFilter implements Filter {

    private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
    private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
    private static final AsciiString CONNECTION = new AsciiString("Connection");
    private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
    private static final AsciiString DATE = new AsciiString("Date");

    @Override
    public Result invoke(Invoker invoker, Invocation invocation) {
        if (invocation.getContent() instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) invocation.getContent();

            HttpContent httpContent = transformHttpRequest(request);
            invocation.setContent(httpContent);

            if (getNextFilter() != null) {
                return getNextFilter().invoke(invoker, invocation);
            } else {
                return new Result(httpContent);
            }
        }
        return null;
    }

    /**
     * 转换为http request content
     * @param request
     * @return
     */
    private HttpContent transformHttpRequest(FullHttpRequest request) {
        HttpContent httpContent = new HttpContent();
        httpContent.setHeaderList(buildHttpHeader(request.headers()));
        httpContent.setUrl(request.uri());
        httpContent.setMethod(request.method().name());
        httpContent.setBody(buildHttpBody(request));

        return httpContent;
    }

    /**
     * 构建http header
     * @param httpHeaders
     * @return
     */
    private List<ProtocolHeader> buildHttpHeader(HttpHeaders httpHeaders) {
        List<ProtocolHeader> headerList = new ArrayList<>();

        for(Map.Entry<String, String> entry : httpHeaders.entries()) {
            ProtocolHeader protocolHeader = new ProtocolHeader();
            protocolHeader.setHeaderKey(entry.getKey());
            protocolHeader.setHeaderValue(entry.getValue());

            headerList.add(protocolHeader);
        }

        return headerList;
    }

    /**
     * 构建 http body
     * @param request
     * @return
     */
    private String buildHttpBody(FullHttpRequest request) {
        if (request.method().name().equals(HttpMethod.POST.name())) {
            int contentLength = request.headers().getInt(CONTENT_LENGTH);
            byte[] bodyBytes = new byte[contentLength];
            request.content().readBytes(bodyBytes);

            //TODO 编码问题后续需要处理
            String requestBody = new String(bodyBytes);
            return requestBody;
        }
        return null;
    }
}
