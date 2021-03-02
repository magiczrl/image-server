package com.cn.image.dao.cache;

import com.cn.image.common.BizException;
import com.cn.image.common.ResMsg;
import com.cn.image.context.CacheWrapper;
import com.cn.image.dao.mapper.ImageInfoMapper;
import com.cn.image.model.ImageInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@Service
public class CachedImageInfo implements ImageInfoMapper {

    @Autowired
    private CacheWrapper cacheWrapper;
    @Autowired
    private ImageInfoMapper imageInfoMapper;

    @Override
    public ImageInfo findByMd5(String md5) {
        ImageInfo imageInfo = cacheWrapper.get("IMG_MD5_" + md5);
        if (imageInfo != null) {
            return imageInfo;
        }
        imageInfo = imageInfoMapper.findByMd5(md5);
        if (imageInfo != null) {
            cacheWrapper.put("IMG_MD5_" + md5, imageInfo, 300);
        }
        return imageInfo;
    }

    @Override
    public List<ImageInfo> findByImageName(String imageName) {
        List<ImageInfo> list = cacheWrapper.get("IMG_NAME_" + imageName);
        if (CollectionUtils.isNotEmpty(list)) {
            return list;
        }
        list = imageInfoMapper.findByImageName(imageName);
        if (CollectionUtils.isNotEmpty(list)) {
            cacheWrapper.put("IMG_NAME_" + imageName, (Serializable) list, 300);
        }
        return list;
    }

    @Override
    public void insert(ImageInfo imageInfo) {
        imageInfoMapper.insert(imageInfo);
    }

    @Override
    public void update(ImageInfo imageInfo) {
        if (imageInfo == null || imageInfo.getMd5() == null) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "update request error[1]");
        }
        ImageInfo imageInfoDb = findByMd5(imageInfo.getMd5());
        if (imageInfoDb == null) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "update request error[2]");
        }
        cacheWrapper.delete("IMG_MD5_" + imageInfo.getMd5());
        cacheWrapper.delete("IMG_NAME_" + imageInfoDb.getImageName());
        imageInfoMapper.update(imageInfo);
    }

    @Override
    public List<ImageInfo> findAll() {
        return imageInfoMapper.findAll();
    }

    @Override
    public List<ImageInfo> searchImage(Map<String, Object> map) {
        return imageInfoMapper.searchImage(map);
    }

    @Override
    public void deleteByMd5(String md5) {
        imageInfoMapper.deleteByMd5(md5);
    }

    @Override
    public int countImage() {
        return imageInfoMapper.countImage();
    }

}
