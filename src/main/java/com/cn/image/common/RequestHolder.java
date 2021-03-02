package com.cn.image.common;

import com.alibaba.fastjson.JSONObject;

/**
 * @Type RequestHolder.java
 * @Desc 
 * @author Z.R.L
 * @date 2020年6月11日 下午3:16:17
 * @version 
 */
public class RequestHolder {

    private static ThreadLocal<JSONObject> requestLocal = new ThreadLocal<JSONObject>();

    /**
     * 
     * @param requestJson
     */
    public static void set(JSONObject requestJson) {
        requestLocal.set(requestJson);
    }

    /**
     * 
     * @return
     */
    public static JSONObject get() {
        return requestLocal.get();
    }

    /**
     * 
     */
    public static void clear() {
        requestLocal.remove();
    }

}

/**
 * Revision history
 * -------------------------------------------------------------------------
 * 
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2020年6月11日 Z.R.L create
 */
