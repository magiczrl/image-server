package com.cn.image.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;

/**
 * @Desc 
 * @author Z
 * @date 2020年9月14日 下午3:55:23
 */
public class Utils {

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * 读取接口请求数据
     * @param request
     * @return
     * @throws IOException
     */
    public static String readReqMsg(HttpServletRequest request) throws IOException {
        StringBuffer reqMsg = new StringBuffer();
        BufferedReader reader = request.getReader();
        String str = "";
        while ((str = reader.readLine()) != null) {
            reqMsg.append(str);
        }
        return reqMsg.toString();
    }

    /**
     * 获取源IP
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip.trim())) {
            return ip;
        }
        ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null) {
            return null;
        }
        return ip.split(",\\s*")[0];
    }

    /**
     * 读取请求：字符串形式
     * @param request
     * @return
     */
    public static String readReqStr(HttpServletRequest request) {
        try {
            return readReqMsg(request);
        } catch (Exception e) {
            logger.warn("Error request, exception message = {}", e.getMessage());
            logger.warn("Error request, ip = {}", getIpAddr(request));
        }
        return null;
    }

    /**
     * 读取请求：转成JSON格式
     * @param request
     * @param decode true|需要urldecode, false|不需要
     * @return
     */
    public static JSONObject readReqJson(HttpServletRequest request, boolean decode) {
        try {
            String msg = readReqStr(request);
            // logger.warn("readReqJson {}", msg);
            if (StringUtils.isBlank(msg)) {
                return null;
            }
            if (decode) {
                msg = URLDecoder.decode(msg, "UTF-8");
            }
            // 指定不排序
            return JSONObject.parseObject(msg, Feature.OrderedField);
        } catch (Exception e) {
            logger.warn("Error JSON, exception message = {}", e.getMessage());
            logger.warn("Error JSON, ip = {}", getIpAddr(request));
        }
        return null;
    }

    /**
     * 
     * @param bytes
     * @return
     */
    public static byte[] byteConvert(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = (byte) ((bytes[i] * 31 >> 1) & 0xFF);
        }
        return result;
    }

    /**
     * @param text
     * @return
     * @throws Exception
     */
    public static String md5(String text) {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text);
        // logger.info("MD5加密前：[]，加密后的字符串为：encodeStr=" + encodeStr);
        return encodeStr;
    }

    /**
     * 
     * @param len
     * @return
     */
    public static String genNonce(int len) {
        StringBuffer buf = new StringBuffer();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        char[] charArray = chars.toCharArray();
        for (int i = 0; i < len; i++) {
            double random = Math.random();
            buf.append(charArray[(int) (random * charArray.length)]);
        }
        return buf.toString();
    }

    /**
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 
     * @param time 12:00
     * @return 当前天+time的date
     */
    public static Date parseDate(String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }

        String[] arr = StringUtils.split(time, ":");
        if (arr == null || arr.length != 2) {
            return null;
        }

        String hourStr = arr[0];
        String minStr = arr[1];
        if (!StringUtils.isNumeric(hourStr) || !StringUtils.isNumeric(minStr)) {
            return null;
        }

        LocalDateTime ldt = LocalDateTime.now();
        ldt = ldt.withHour(Integer.parseInt(hourStr)).withMinute(Integer.parseInt(minStr))
                .withSecond(0).withNano(0);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     *
     * @param message
     * @param secret
     * @return
     */
    public static String hmacSha256(String message, String secret) {
        String result = null;
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            hmacSHA256.init(secretKey);
            byte[] bytes = hmacSHA256.doFinal(message.getBytes());
            result = Utils.byteArrayToHexString(bytes);
        } catch (Exception e) {
            logger.error("hmacSha256 fail, ", e);
        }
        return result;
    }

    /**
     * 字节数组转字符串
     * @param bArray 字节数组
     * @return 字符串
     */
    public static String byteArrayToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}
