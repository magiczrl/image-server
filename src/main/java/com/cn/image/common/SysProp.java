package com.cn.image.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Desc 
 * @author Z
 * @date 2020年9月14日 下午3:55:23
 */
@Component
public class SysProp {

    @Value("${imgUrl}")
    private String imgUrl;

    @Value("${login.limitNum:8}")
    private int limitNum;
    @Value("${login.limitTime:300}")
    private int limitTime;

    @Value("${login.limitIpNum:100}")
    private int limitIpNum;
    @Value("${login.limitIpTime:300}")
    private int limitIpTime;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }

    public int getLimitIpNum() {
        return limitIpNum;
    }

    public void setLimitIpNum(int limitIpNum) {
        this.limitIpNum = limitIpNum;
    }

    public int getLimitIpTime() {
        return limitIpTime;
    }

    public void setLimitIpTime(int limitIpTime) {
        this.limitIpTime = limitIpTime;
    }

}
