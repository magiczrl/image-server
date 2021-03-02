package com.cn.image.common;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
public class BizException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int returnCode;

    private String returnMessage;

    public BizException(int returnCode, String returnMessage) {
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
