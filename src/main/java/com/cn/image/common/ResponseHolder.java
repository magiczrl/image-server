package com.cn.image.common;

import com.alibaba.fastjson.JSONObject;

/**
 * @Desc 
 * @author Z.R.L
 * @date 2020年8月19日 上午10:47:18
 */
public class ResponseHolder {

    private static ThreadLocal<JSONObject> responseLocal = new ThreadLocal<JSONObject>();

    /**
     * 
     * @param responseJson
     */
    public static void set(JSONObject responseJson) {
        responseLocal.set(responseJson);
    }

    /**
     * 
     * @return
     */
    public static JSONObject get() {
        return responseLocal.get();
    }

    /**
     * 
     */
    public static void clear() {
        responseLocal.remove();
    }

}
