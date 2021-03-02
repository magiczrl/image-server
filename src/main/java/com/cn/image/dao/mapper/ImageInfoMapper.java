package com.cn.image.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.cn.image.model.ImageInfo;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月22日 下午2:09:26
 */
@Mapper
public interface ImageInfoMapper {

    /**
     * 
     * @param md5
     * @return
     */
    public ImageInfo findByMd5(String md5);

    /**
     * 
     * @param imageName
     * @return
     */
    public List<ImageInfo> findByImageName(String imageName);

    /**
     * 
     * @return
     */
    public List<ImageInfo> findAll();

    /**
     * @param map
     * @return
     */
    public List<ImageInfo> searchImage(Map<String, Object> map);

    /**
     * 
     * @param imageInfo
     */
    public void insert(ImageInfo imageInfo);

    /**
     * 
     * @param imageInfo
     */
    public void update(ImageInfo imageInfo);

    /**
     * 
     * @param md5
     */
    public void deleteByMd5(String md5);

    /**
     * 
     * @return
     */
    public int countImage();
}
