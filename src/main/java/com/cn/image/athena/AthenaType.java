package com.cn.image.athena;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
public class AthenaType {

    private static final Map<String, String> TYPE_MAP = new HashMap<String, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            put("Lcom/cn/image/controller/UserController;genPublicKey(Ljavax/servlet/http/HttpServletRequest;)",
                    "231000");
            put("Lcom/cn/image/controller/UserController;login(Ljavax/servlet/http/HttpServletRequest;Ljava/lang/String;Ljava/lang/String;)",
                    "231001");
            put("Lcom/cn/image/controller/ImageController;uploadInnerForward(Lorg/springframework/web/multipart/MultipartFile;)",
                    "231002");
            put("Lcom/cn/image/controller/ImageController;findAllImageLabel()", "231003");
            put("Lcom/cn/image/controller/ImageController;browserImageByMd5(Ljava/lang/String;)",
                    "231004");
            put("Lcom/cn/image/controller/ImageController;browserImageByImageName(Ljava/lang/String;)",
                    "231005");
            put("Lcom/cn/image/controller/ImageController;deleteByMd5(Lcom/alibaba/fastjson/JSONObject;)",
                    "231006");
            put("Lcom/cn/image/controller/ImageController;upload(Lorg/springframework/web/multipart/MultipartFile;)",
                    "231007");
            put("Lcom/cn/image/controller/ImageController;updateImageInfo(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)",
                    "231008");
            put("Lcom/cn/image/controller/ImageController;imgList(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)",
                    "231009");
        }
    };

    public static String get(String key) {
        return TYPE_MAP.get(key);
    }

    public static final String MAX = "231010";
}
