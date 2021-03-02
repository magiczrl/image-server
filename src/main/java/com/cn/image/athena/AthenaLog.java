package com.cn.image.athena;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年6月3日 上午10:42:14
 */
public class AthenaLog {

    private static final Logger logger = LoggerFactory.getLogger(AthenaLog.class);

    /**
     * 用户类型，1:手机号 2:邮箱 3:MAC
     */
    private String loginIdType;
    /**
     * 手机号/邮箱/MAC
     */
    private String loginId;
    /**
     * 操作类型
     */
    private String operType;
    /**
     * 操作返回码
     */
    private String operStatus;
    /**
     * 0：未知, 1：web （含小程序、H5）, 2: client （含插件后台）, 3: 内部模块间接口, 4: 第三方平台间接口
     */
    private String appType;
    /**
     * 会话ID
     */
    private String sessionId;
    /**
     * 请求时间,精确到毫秒2014-09-09 09:09:09:222
     */
    private LocalDateTime requestDate;
    /**
     * 响应时间,精确到毫秒2014-09-09 09:09:09:222
     */
    private LocalDateTime responseDate;
    /**
     * 事务ID
     */
    private String transactionId;
    /**
     * 源IP
     */
    private String fromIP;
    /**
     * 服务地址,标明在哪个服务器的哪个节点执行的,IP:Port形式
     */
    private String serverAddr;
    /**
     * 消息体
     */
    private JSONObject message;

    private JSONObject request;
    private JSONObject response;

    private JSONObject result;

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String getLoginIdType() {
        return loginIdType;
    }

    public void setLoginIdType(String loginIdType) {
        this.loginIdType = loginIdType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getOperStatus() {
        return operStatus;
    }

    public void setOperStatus(String operStatus) {
        this.operStatus = operStatus;
    }

    public HttpServletRequest httpServletRequest;

    public AthenaLog(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getRequestDate() {
        return this.requestDate;
    }

    public String getRequestDateStr() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        return df.format(requestDate);
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getReponseDateStr() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        return df.format(responseDate);
    }

    public LocalDateTime getResponseDate() {
        return this.responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromIP() {
        return fromIP;
    }

    public void setFromIP(String fromIP) {
        this.fromIP = fromIP;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    public JSONObject getRequest() {
        return request;
    }

    public void setRequest(JSONObject request) {
        this.request = request;
    }

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject response) {
        this.response = response;
    }

    public static final String IP_ADDR_STR;
    static {
        String ip = StringUtils.EMPTY;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            byte[] ipAddr = addr.getAddress();
            for (int i = 0; i < ipAddr.length; i++) {
                if (i > 0) {
                    ip += ".";
                }
                ip += ipAddr[i] & 0xFF;
            }
        } catch (UnknownHostException e) {
            logger.warn(e.getMessage(), e);
        }
        IP_ADDR_STR = ip;
    }

    /**
     * 
     * @param request
     * @return
     */
    public static String obtainServerAddr(HttpServletRequest request) {
        return IP_ADDR_STR + ":" + (request != null ? request.getLocalPort() : "NULL");
    }

}

/**
 * Revision history
 * -------------------------------------------------------------------------
 * 
 * Date Author Note
 * -------------------------------------------------------------------------
 * 2020年6月3日 Z.R.L create
 */
