package com.cn.image.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author magiczrl@foxmail.com
 */
public class HmacSha256 {

    private static Logger logger = LoggerFactory.getLogger(HmacSha256.class);
    private static final String ALGORITHM_HMAC_SHA256 = "HmacSHA256";

    /**
     * <p>
     *  (Hash-based Message Authentication Code) SHA256 <br/>
     *  
     *  eg: <br/> 
     *    message: 123_abc-ABC, secret: 456efg <br/> 
     *    result: 2cfd39f4c64ca7fb055536ee5174edd560f94b00d1ff68a250f9c9657743208c
     * </p> 
     * @param message
     * @param secret
     * @return
     */
    public static String hmacSha256(String message, String secret) {
        String result = null;
        try {
            Mac hmacSHA256 = Mac.getInstance(ALGORITHM_HMAC_SHA256);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), ALGORITHM_HMAC_SHA256);
            hmacSHA256.init(secretKey);
            byte[] bytes = hmacSHA256.doFinal(message.getBytes());
            result = bytesToHexString(bytes);
        } catch (Exception e) {
            logger.warn("hmacSha256 fail, message {}", message);
            logger.warn(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 字节数组转字符串
     *
     * @param bytes 字节数组
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuffer bufString = new StringBuffer(bytes.length);
        String tmpHex;
        for (int i = 0; i < bytes.length; i++) {
            tmpHex = Integer.toHexString(0xff & bytes[i]);
            if (tmpHex.length() < 2) {
                bufString.append(0);
            }
            bufString.append(tmpHex.toLowerCase());
        }
        return bufString.toString();
    }

    //    public static void main(String[] args) {
    //        String message = "123_abc-ABC";
    //        String secret = "456efg";
    //        String sign = HmacSha256.hmacSha256(message, secret);
    //        System.out.println("result: " + sign);
    //    }
}
