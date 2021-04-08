package com.cn.image.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.cn.image.common.ActionResponse;
import com.cn.image.common.Constants;
import com.cn.image.common.ResMsg;
import com.cn.image.common.Utils;
import com.cn.image.service.UserService;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月16日 下午4:51:17
 */
@RestController
public class UserController {

    // private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/user/login" }, method = RequestMethod.POST, produces = {
            "application/json;charset=UTF-8" })
    public ActionResponse login(HttpServletRequest request, @RequestBody JSONObject requestJson) {
        ActionResponse ar = new ActionResponse();
        String ip = Utils.getIpAddr(request);
        String username = requestJson.getString("username");
        String password = requestJson.getString("password");
        ar.setData(userService.login(ip, username, password));
        ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                ResMsg.SUCCESS.getReturnMessage());
        return ar;
    }

    /**
     * 登录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/user/getPublicKey" }, method = RequestMethod.GET, produces = {
            "application/json;charset=UTF-8" })
    public ActionResponse genPublicKey(HttpServletRequest request) {
        ActionResponse ar = new ActionResponse();
        ar.setData("\"-----BEGIN PUBLIC KEY-----" + Constants.PUB_KEY + "-----END PUBLIC KEY-----\"");
        ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                ResMsg.SUCCESS.getReturnMessage());
        return ar;
    }
}
