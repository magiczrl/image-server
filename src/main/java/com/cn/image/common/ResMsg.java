package com.cn.image.common;

/**
 * @Desc 
 * @author Z
 * @date 2020年9月14日 下午3:55:23
 */
public enum ResMsg {

    SUCCESS(0, "success"),
    FAIL(1, "fail"),;

    private int returnCode;
    private String returnMessage;

    private ResMsg(int returnCode, String returnMessage) {
        this.returnCode = returnCode;
        this.returnMessage = returnMessage;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

}
