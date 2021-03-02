package com.cn.image.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
public class ActionResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7704852649988429280L;

    private static final String DATA_KEY = "data";

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    private static final Map<Integer, String> MESSAGES = new HashMap<Integer, String>();

    private int returnCode = SUCCESS;
    private String returnMessage = MESSAGES.get(SUCCESS);

    private Map<String, Object> datas = new HashMap<String, Object>();

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
        this.returnMessage = MESSAGES.get(returnCode);
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public void setReturnCodeAndMessage(int returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

    public void setData(Object value) {
        datas.put(DATA_KEY, value);
    }

    /**
     * 
     */
    public void reset() {
        setReturnCode(SUCCESS);
        datas.clear();
    }

    /**
     * 
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        datas.put(key, value);
    }

    /**
     * 
     * @param values
     */
    public void putAll(Map<String, Object> values) {
        datas.putAll(values);
    }

    public Object getData() {
        if (datas.size() == 1) {
            Object data = datas.get(DATA_KEY);
            if (data != null) {
                return data;
            }
        }
        return datas;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

}
