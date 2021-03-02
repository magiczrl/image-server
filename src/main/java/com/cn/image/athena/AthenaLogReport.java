package com.cn.image.athena;

import com.alibaba.fastjson.JSONObject;
import com.cn.image.common.Utils;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年6月3日 上午11:29:36
 */
public class AthenaLogReport {

    private static Logger logger = LoggerFactory.getLogger("ATHENA");

    private static final String FIELD_SEPARATOR = "|";
    /**
     * 手机号
     */
    private static final String LOGIN_ID_TYPE = "3";

    private static String toOut(String str) {
        return StringUtils.isBlank(str) ? "NULL" : str;
    }

    /**
     * 处理打印Athena日志
     * @param athenaLog
     * @param requestDate
     */
    public static void reportComplete(AthenaLog athenaLog, LocalDateTime requestDate) {
        athenaLog.setRequestDate(requestDate);
        if (athenaLog.httpServletRequest != null) {
            String ip = Utils.getIpAddress(athenaLog.httpServletRequest);
            athenaLog.setFromIP(ip == null ? "NULL" : ip.split(",")[0]);
            athenaLog.setServerAddr(AthenaLog.obtainServerAddr(athenaLog.httpServletRequest));
        } else {
            athenaLog.setFromIP("unknown");
            athenaLog.setServerAddr("unknown");
        }
        String loginId = athenaLog.getLoginId();
        String operType = athenaLog.getOperType();
        athenaLog.setResponseDate(LocalDateTime.now());
        long costtime = 0l;
        if (athenaLog.getRequestDate() != null) {
            costtime = Duration.between(athenaLog.getRequestDate(), athenaLog.getResponseDate())
                    .toMillis();
        }

        List<String> ids = ImmutableList.of(LOGIN_ID_TYPE, toOut(loginId), toOut(operType),
                String.valueOf(athenaLog.getOperStatus()), toOut(athenaLog.getAppType()),
                toOut(athenaLog.getSessionId()), toOut(athenaLog.getRequestDateStr()),
                toOut(athenaLog.getReponseDateStr()), toOut(athenaLog.getTransactionId()),
                toOut(athenaLog.getFromIP()), toOut(athenaLog.getServerAddr()),
                getMessage(athenaLog, athenaLog.getResponse(), costtime));
        String msg = FIELD_SEPARATOR + String.join(FIELD_SEPARATOR, ids);
        logger.info(msg);
    }

    private static String getMessage(AthenaLog athenaLog, JSONObject response, long costtime) {
        JSONObject json = new JSONObject();
        JSONObject reqJson = athenaLog.getRequest();
        if (reqJson != null && reqJson.containsKey("password")) {
            reqJson.put("password", "...");
        }
        json.put("request", reqJson);
        json.put("response", response);
        json.put("costtime", costtime);
        return json.toString();
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
