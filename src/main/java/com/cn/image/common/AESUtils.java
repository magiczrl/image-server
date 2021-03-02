package com.cn.image.common;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
public class AESUtils {

    private static Logger logger = LoggerFactory.getLogger(AESUtils.class);

    public static final String ENCODING = "UTF-8";

    /**
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] encryptByByte(byte[] key, byte[] data) {
        try {
            SecretKeySpec skey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey,
                    new IvParameterSpec(new byte[cipher.getBlockSize()]));
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.debug("encrypt error, data {}", data);
        }
        return null;
    }

    /**
     * 
     * @param secretKey
     * @param dataBytes
     * @return
     */
    public static byte[] decrypt(String secretKey, byte[] dataBytes) {
        try {
            return decrypt(secretKey.getBytes(ENCODING), dataBytes);
        } catch (UnsupportedEncodingException e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 
     * @param secretKey
     * @param data
     * @return
     */
    public static String decrypt(byte[] secretKey, String data) {
        return new String(decrypt(secretKey, Base64.getDecoder().decode(data)));
    }

    /**
     * 
     * @param secretKey
     * @param byteMi
     * @return
     */
    public static byte[] decrypt(byte[] secretKey, byte[] byteMi) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key,
                    new IvParameterSpec(new byte[cipher.getBlockSize()]));
            return cipher.doFinal(byteMi);
        } catch (Exception e) {
            logger.warn("decrypt error, data {}", byteMi);
        }
        return null;
    }

}
