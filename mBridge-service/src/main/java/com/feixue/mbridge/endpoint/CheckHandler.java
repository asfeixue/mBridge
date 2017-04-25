package com.feixue.mbridge.endpoint;

import com.alibaba.fastjson.JSON;
import com.feixue.mbridge.domain.report.CheckReport;
import com.feixue.mbridge.domain.protocol.ProtocolHeader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 报文实体对比，校验
 * Created by zxxiao on 15/11/21.
 */
public class CheckHandler {

    public static CheckReport checkResponseBody(Object body, Object mockBody) {
        return checkStructure(body.toString(), mockBody.toString());
    }

    /**
     * header校验
     * @param checkHeaders  校验header
     * @param mockHeaders   约定header
     * @return
     */
    public static CheckReport checkHeader(List<ProtocolHeader> checkHeaders, List<ProtocolHeader> mockHeaders) {
        if (mockHeaders == null || mockHeaders.isEmpty()) {
            return new CheckReport(true);
        }

        List<String> warnInfo = new ArrayList<>();
        for(ProtocolHeader protocolHeader : mockHeaders) {
            boolean hasHeader = false;
            for(ProtocolHeader header : checkHeaders) {
                if (header.getHeaderKey().equalsIgnoreCase(protocolHeader.getHeaderKey())) {
                    hasHeader = true;
                    break;
                }
            }
            if (!hasHeader) {
                warnInfo.add("header缺失:" + protocolHeader.getHeaderKey());
            }
        }
        if (warnInfo.isEmpty()) {
            return new CheckReport(true);
        } else {
            return new CheckReport(false, warnInfo);
        }
    }

    private static CheckReport checkStructure(Object body, Object mockBody) {
        if (body instanceof String && mockBody instanceof String) {
            if (StringUtils.isEmpty((String) body) && StringUtils.isNotEmpty((String) mockBody)) {
                return new CheckReport(false, "数据结构不一致！结构缺失：" + mockBody);
            } else if (((String) body).startsWith("{") && ((String) mockBody).startsWith("{")) {
                //对象校验
                return checkStructure(JSON.parseObject(body.toString()),
                        JSON.parseObject(mockBody.toString()));
            } else if (((String) body).startsWith("[") && ((String) mockBody).startsWith("[")) {
                //数组校验
                return checkStructure(JSON.parseArray(body.toString()),
                        JSON.parseArray(mockBody.toString()));
            } else {
                //不包含结构时，数据类型一致即可
                if (body instanceof String && mockBody instanceof String) {
                    return new CheckReport(true);
                } else if (body instanceof Integer && mockBody instanceof Integer) {
                    return new CheckReport(true);
                } else if (body instanceof Long && mockBody instanceof Long) {
                    return new CheckReport(true);
                } else if (body instanceof Boolean && mockBody instanceof Boolean) {
                    return new CheckReport(true);
                } else {
                    return new CheckReport(false, "数据类型不一致！约定类型为："
                            + body.getClass().getSimpleName() + ";  校验类型为：" + mockBody.getClass().getSimpleName());
                }
            }
        } else if (body instanceof Map && mockBody instanceof Map) {
            Map<String, Object> bodyMap = (Map<String, Object>) body;
            Map<String, Object> mockBodyMap = (Map<String, Object>) mockBody;
            Collection<String> keys = CollectionUtils.subtract(mockBodyMap.keySet(), bodyMap.keySet());
            if (!keys.isEmpty()) {
                return new CheckReport(false, "在校验的对象结构中，缺失如下字段：" + keys.toString());
            }

            for(Map.Entry<String, Object> mockBodyEntry : mockBodyMap.entrySet()) {
                String mockBodyKey = mockBodyEntry.getKey();
                Object mockBodyValue = mockBodyEntry.getValue();
                Object bodyValue = bodyMap.get(mockBodyKey);

                CheckReport checkReport = checkStructure(bodyValue, mockBodyValue);
                if (!checkReport.isStatus()) {
                    return checkReport;
                }
            }
            return new CheckReport(true);

        } else if (body instanceof List && mockBody instanceof List) {
            List bodyList = (List) body;
            List mockBodyList = (List) mockBody;
            if (mockBodyList.isEmpty()) {
                return new CheckReport(true);
            } else if (bodyList.isEmpty()) {
                return new CheckReport(false, "缺乏如下内容：" + mockBody.toString());
            } else {
                return checkStructure(bodyList.get(0), mockBodyList.get(0));
            }
        } else if (body instanceof Integer && mockBody instanceof Integer) {
            return new CheckReport(true);
        } else if (body instanceof Long && mockBody instanceof Long) {
            return new CheckReport(true);
        } else if (body instanceof Float && mockBody instanceof Float) {
            return new CheckReport(true);
        } else if (body instanceof Double && mockBody instanceof Double) {
            return new CheckReport(true);
        } else if (body instanceof Boolean && mockBody instanceof Boolean) {
            return new CheckReport(true);
        } else if (body instanceof BigDecimal && mockBody instanceof BigDecimal) {
            return new CheckReport(true);
        } else if(body == null && mockBody == null) {
            return new CheckReport(true);
        } else {
            if (body == null) {
                if (mockBody instanceof List) {
                    if (((List) mockBody).isEmpty()) {
                        return new CheckReport(true);
                    } else {
                        return new CheckReport(false, "缺失如下内容：" + mockBody.toString());
                    }
                } else if (mockBody instanceof Map) {
                    if (((Map) mockBody).isEmpty()) {
                        return new CheckReport(true);
                    } else {
                        return new CheckReport(false, "缺失如下内容：" + mockBody.toString());
                    }
                } else if (mockBody instanceof String) {
                    if (((String) mockBody).isEmpty()) {
                        return new CheckReport(true);
                    } else {
                        return new CheckReport(false, "在校验数据结构中缺失如下内容：" + mockBody.toString());
                    }
                }
            } else {
                //2者类型不一致
                return new CheckReport(false, "结构不一致！约定结构：" + mockBody.toString());
            }
        }

        return new CheckReport(false, "无有效检查！");
    }
}
