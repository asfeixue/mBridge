package com.feixue.mbridge.util;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * Created by zxxiao on 16/8/30.
 */
public class CloneUtil {

    /**
     * 克隆map
     * @param map
     * @return
     */
    public static Map cloneMap(Map map) {
        String srcStr = JSON.toJSONString(map);
        return JSON.parseObject(srcStr, Map.class);
    }
}
