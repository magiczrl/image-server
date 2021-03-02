package com.cn.image.common;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Desc 
 * @author Z
 * @date 2020年9月14日 下午3:55:23
 */
public class RSA2Coder {

    private static final Logger logger = LoggerFactory.getLogger(RSA2Coder.class);

    /**
     * 密钥长度，DH算法的默认密钥长度是1024
     * 密钥长度必须是64的倍数，在512到65536位之间
     */
    public static final int KEY_SIZE = 2048;
    //非对称密钥算法
    public static final String KEY_ALGORITHM = "RSA";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String ENCODE_ALGORITHM = "SHA-256";
    public static final byte[] PRI_KEY = AESUtils.decrypt(Constants.CONFIG_FACTOR,
            HexUtils.fromHexString(Constants.ENC_PRI_KEY));

    /**
     * 生成密钥对
     * 
     * @return
     */
    public static Map<String, String> genKeyPair() {
        try {
            //实例化密钥生成器
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            //将密钥存储在map中
            Map<String, String> keyMap = new HashMap<String, String>();
            keyMap.put(PUBLIC_KEY, Base64.encodeBase64String(publicKey.getEncoded()));
            keyMap.put(PRIVATE_KEY, Base64.encodeBase64String(privateKey.getEncoded()));
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * $ openssl genrsa -out private_key.pem 1024
     *  pkcs1格式私钥转为pkcs8格式私钥
     * $ openssl pkcs8 -topk8 -inform PEM -in private_key.pem -outform PEM -nocrypt -out rsa_private_key.pem --pkcs8格式
     * $ openssl rsa -in rsa_private_key.pem -pubout -out rsa_public_key.pem
     * 
    */

    /**
     * 还原公钥
     * 注意：BASE64后的RSA公私钥需先BASE64解码，不能直接getBytes
     * @param keyBytes
     * @return
     */
    public static PublicKey restorePublicKey(byte[] keyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 还原私钥
     * @param keyBytes
     * @return
     */
    public static PrivateKey restorePrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    //    public static byte[] formatPkcs1ToPkcs8(byte[] keyBytes) throws Exception {
    //        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(
    //                PKCSObjectIdentifiers.pkcs8ShroudedKeyBag); //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag     
    //        ASN1Object asn1Object = ASN1Object.fromByteArray(keyBytes);
    ////        ASN1Encodable privateKey = 
    //        PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(algorithmIdentifier, asn1Object);
    //        byte[] pkcs8Bytes = privKeyInfo.getEncoded();
    //        return pkcs8Bytes;
    //    }

    /**
     * 签名
     * @param privateKey 私钥
     * @param plainText 明文
     * @return
     */
    public static byte[] sign(PrivateKey privateKey, String plainText) {
        MessageDigest messageDigest;
        byte[] signed = null;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plainText.getBytes());
            Signature sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            sign.initSign(privateKey);
            sign.update(messageDigest.digest());
            signed = sign.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            logger.warn(e.getMessage(), e);
        }
        return signed;
    }

    /**
     * 验签
     * @param publicKey 公钥
     * @param plainData 明文
     * @param signed 签名
     */
    public static boolean verifySign(PublicKey publicKey, String plainData, byte[] signed) {
        MessageDigest messageDigest;
        boolean signedSuccess = false;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plainData.getBytes());
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(messageDigest.digest());
            signedSuccess = verifySign.verify(signed);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            logger.warn(e.getMessage(), e);
        }
        return signedSuccess;
    }

    /**
     * RSA解密
     * @param plainData
     * @param publicKey
     * @return
     */
    public static String publicKeyEncrypt(String plainData, PublicKey publicKey) {
        Cipher cipher = null;
        try {
            byte[] data = plainData.getBytes("UTF-8");
            cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(data);
            return HexUtils.toHexString(output);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    /**
     * RSA解密
     * @param encBytes
     * @param privateKey
     * @return
     */
    public static String privateKeyDecrypt(byte[] encBytes, PrivateKey privateKey) {
        Cipher cipher = null;
        try {
            // byte[] data = HexUtils.fromHexString(encData);
            cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(encBytes);
            return new String(output, "UTF-8");
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }

    //    public static void main(String[] args) {
    //        System.out.println(genKeyPair());
    //        String privateKeyOr = "xx";
    //        System.out.println("ENC_PRI_KEY:" + HexUtils.toHexString(AESUtils.encryptByByte(Constants.CONFIG_FACTOR,
    //                Base64.decodeBase64(privateKeyOr))));
    //        String plain = "xx";
    //        PublicKey publicKey = restorePublicKey(Base64.decodeBase64(Constants.PUB_KEY));
    //        String encrypted = publicKeyEncrypt(plain, publicKey);
    //        System.out.println("encrypted:" + encrypted);
    //        PrivateKey privateKey = restorePrivateKey(PRI_KEY);
    //        System.out
    //                .println("decrypted:" + privateKeyDecrypt(HexUtils.fromHexString(encrypted), privateKey));
    //    }
}
