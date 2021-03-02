package com.cn.image.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月16日 下午4:51:48
 */
public interface UserService {

    /**
     * @param ip
     * @param username
     * @param password
     * @return
     */
    public JSONObject login(String ip, String username, String password);
}
