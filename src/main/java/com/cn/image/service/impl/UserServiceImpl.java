package com.cn.image.service.impl;

import java.util.Base64;

import org.apache.commons.lang3.Validate;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.cn.image.common.AESUtils;
import com.cn.image.common.BizException;
import com.cn.image.common.Constants;
import com.cn.image.common.HmacSha256;
import com.cn.image.common.RSA2Coder;
import com.cn.image.common.ResMsg;
import com.cn.image.common.SysProp;
import com.cn.image.common.Utils;
import com.cn.image.context.CacheWrapper;
import com.cn.image.dao.mapper.UserAccountMapper;
import com.cn.image.model.UserAccount;
import com.cn.image.service.UserService;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月16日 下午4:52:05
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private CacheWrapper cacheWrapper;
    @Autowired
    private SysProp sysProp;

    @Override
    public JSONObject login(String ip, String username, String password) {
        ipRisk(ip);

        // 账号密码校验
        try {
            Validate.notBlank(username);
            Validate.notBlank(password);
        } catch (Exception e) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), ResMsg.FAIL.getReturnMessage());
        }
        password = RSA2Coder.privateKeyDecrypt(Base64.getDecoder().decode(password),
                RSA2Coder.restorePrivateKey(RSA2Coder.PRI_KEY));

        String encUserName = HexUtils
                .toHexString(AESUtils.encryptByByte(Constants.USER_FACTOR, username.getBytes()));
        UserAccount userAccount = userAccountMapper.findUserAccountByUserName(encUserName);
        if (userAccount == null) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "user error");
        }
        String dbPass = userAccount.getPasswd();
        String sha256Pass = HmacSha256.hmacSha256(password, Constants.PASS_FACTOR);
        userPassRisk(encUserName, dbPass, sha256Pass);
        JSONObject response = new JSONObject();
        String token = System.currentTimeMillis() + Utils.genNonce(32);
        cacheWrapper.put(Constants.CACHE_TOKEN_KEY + token, encUserName, 3600);
        response.put("token", token);
        return response;
    }

    private void userPassRisk(String encUserName, String dbPass, String sha256Pass) {
        Integer errorNum = cacheWrapper.get(Constants.CACHE_LOGIN_RISK_KEY + encUserName);
        if (errorNum != null && errorNum > sysProp.getLimitNum()) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "forbidden[1]");
        }
        if (!sha256Pass.equals(dbPass)) {
            cacheWrapper.put(Constants.CACHE_LOGIN_RISK_KEY + encUserName,
                    (errorNum == null ? 1 : errorNum + 1), sysProp.getLimitTime());
            throw new BizException(ResMsg.FAIL.getReturnCode(), "username or password error");
        }
    }

    private void ipRisk(String ip) {
        Integer num = cacheWrapper.get(Constants.CACHE_LOGIN_RISK_IP_KEY + ip);
        if (num != null && num > sysProp.getLimitIpNum()) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "forbidden[2]");
        }
        cacheWrapper.put(Constants.CACHE_LOGIN_RISK_IP_KEY + ip, (num == null ? 1 : num + 1),
                sysProp.getLimitIpTime());
    }

}
