package com.cn.image.service;

import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:02
 */
public interface ImageService {

    /**
     * 
     * @param file
     * @param label
     * @return
     */
    public JSONObject upload(MultipartFile file, String label);

    /**
     * 
     * @param md5Array
     * @return
     */
    public void deleteByMd5(JSONArray md5Array);

    /**
     * 
     * @param imageLabel
     * @param imageName
     * @param md5
     */
    public void updateImageInfo(String imageLabel, String imageName, String md5);

    /**
     * 
     * @param md5
     * @param imageName
     */
    public byte[] browserImage(String md5, String imageName);

    /**
     * 
     * @return
     */
    public JSONArray getAllImageLabel();

    /**
     * 
     * @param imageName
     * @param imageLabel
     * @param pageNum
     * @param pageSize
     * @return
     */
    public JSONObject imgList(String imageName, String imageLabel, String pageNum, String pageSize);

}
