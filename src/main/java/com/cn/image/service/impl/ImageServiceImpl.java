package com.cn.image.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.image.common.BizException;
import com.cn.image.common.DateUtil;
import com.cn.image.common.HttpUtil;
import com.cn.image.common.ResMsg;
import com.cn.image.common.SysProp;
import com.cn.image.dao.cache.CachedImageInfo;
import com.cn.image.model.ImageInfo;
import com.cn.image.service.ImageService;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private SysProp sysProp;
    @Autowired
    private CachedImageInfo cachedImageInfo;

    @Override
    public JSONObject upload(MultipartFile file, String label) {
        JSONObject dataJson = new JSONObject();
        if (file == null) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), ResMsg.FAIL.getReturnMessage());
        }
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "jpeg");
        String result = null;
        String fileName = "";
        try {
            fileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
            logger.info("file name: {}", fileName);
            result = HttpUtil.postHttpsBinary(sysProp.getImgUrl() + "/upload", header,
                    file.getBytes());
            logger.info("upload result {}", result);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new BizException(ResMsg.FAIL.getReturnCode(), "upload error");
        }
        if (result == null) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "upload fail");
        }
        String md5 = ((JSONObject) JSONObject.parse(result)).getJSONObject("info").getString("md5");
        dataJson.put("md5", md5);
        // 图片name MD5对应关系入库
        ImageInfo imageInfo = cachedImageInfo.findByMd5(md5);
        if (imageInfo == null) {
            imageInfo = new ImageInfo();
            imageInfo.setCreateTime(new Date());
            imageInfo.setUpdateTime(new Date());
            imageInfo.setImageLabel(label);
            imageInfo.setImageName(fileName);
            imageInfo.setMd5(md5);
            cachedImageInfo.insert(imageInfo);
        } else {
            if (StringUtils.isNotBlank(fileName)) {
                imageInfo.setImageName(fileName);
            }
            imageInfo.setImageLabel(label);
            imageInfo.setUpdateTime(new Date());
            cachedImageInfo.update(imageInfo);
        }
        return dataJson;
    }

    @Override
    public void deleteByMd5(JSONArray md5Array) {
        if (md5Array == null || md5Array.isEmpty()) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "md5 is blank");
        }
        try {
            md5Array.stream().forEach(md5 -> {
                try {
                    deleteImage((String) md5);
                } catch (BizException e) {
                    throw e;
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            });
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            throw new BizException(ResMsg.FAIL.getReturnCode(), "delete error");
        }

        return;
    }

    private void deleteImage(String md5) throws Exception {
        // curl -i -H 'Host: 172.28.22.60' -d '1' -H 'Content-Length: 1' -H  'Content-type: jpeg' 
        // "http://172.28.73.49:4869/admin?t=1&md5=9a169cf52753030cfd1feae609eb8725"
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-type", "jpeg");
        String entity = "{}";
        //header.put("Content-Length", String.valueOf(entity.length()));
        String result = HttpUtil.sendHttpsPostRequest(sysProp.getImgUrl() + "/admin?t=1&md5=" + md5,
                header, entity);
        logger.info("deleteByMd5: {}, result: {}", md5, result);
        // {"ret":true,"info":{"md5":"99914b932bd37a50b983c5e7c90ae93b","size":2}}
        boolean ret = JSONObject.parseObject(result).getBooleanValue("ret");
        if (!ret) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "delete fail");
        }
        // 删除数据库中md5值
        cachedImageInfo.deleteByMd5(md5);
    }

    @Override
    public void updateImageInfo(String imageLabel, String imageName, String md5) {
        if (StringUtils.isBlank(md5)) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "md5 error");
        }
        ImageInfo imageInfo = cachedImageInfo.findByMd5(md5);
        if (imageInfo == null) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "md5 not exist");
        }
        if (StringUtils.isNotBlank(imageLabel)) {
            imageInfo.setImageLabel(imageLabel);
        }
        imageInfo.setImageName(imageName);
        cachedImageInfo.update(imageInfo);
    }

    @Override
    public byte[] browserImage(String md5, String imageName) {
        if (StringUtils.isBlank(md5) && StringUtils.isBlank(imageName)) {
            throw new BizException(ResMsg.FAIL.getReturnCode(), "param error");
        }

        if (StringUtils.isNotBlank(imageName)) {
            // 根据imageName 查询返回 MD5
            List<ImageInfo> imageInfoList = cachedImageInfo.findByImageName(imageName);
            if (imageInfoList == null || imageInfoList.isEmpty()) {
                throw new BizException(ResMsg.FAIL.getReturnCode(), "imageName not exist");
            }
            md5 = imageInfoList.get(0).getMd5();
        }
        String url = sysProp.getImgUrl() + "/" + md5 + "?p=0";
        try {
            return HttpUtil.getImageStream(url, null);
        } catch (Exception e) {
            logger.warn("md5 {}, imageName {}, browserImage error", md5, imageName);
            throw new BizException(ResMsg.FAIL.getReturnCode(), ResMsg.FAIL.getReturnMessage());
        }
    }

    @Override
    public JSONArray getAllImageLabel() {
        List<ImageInfo> imageList = cachedImageInfo.findAll();
        if (CollectionUtils.isEmpty(imageList)) {
            return new JSONArray();
        }
        Set<String> uniqLabel = new HashSet<>();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = null;
        for (ImageInfo imageInfo : imageList) {
            if (uniqLabel.contains(imageInfo.getImageLabel())) {
                continue;
            }
            uniqLabel.add(imageInfo.getImageLabel());
            jsonObj = new JSONObject();
            jsonObj.put("label", imageInfo.getImageLabel());
            jsonObj.put("value", imageInfo.getImageLabel());
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

    @Override
    public JSONObject imgList(String imageName, String imageLabel, String pageNum,
                              String pageSize) {
        JSONObject resultData = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(imageName)) {
            map.put("imageName", imageName);
        }
        if (StringUtils.isNotBlank(imageLabel)) {
            map.put("imageLabel", imageLabel);
        }
        int limitStartIndex = 0;
        int limitPageSize = 18;
        if (StringUtils.isNotBlank(pageNum) && StringUtils.isNotBlank(pageSize)) {
            limitStartIndex = (Integer.parseInt(pageNum) - 1) * Integer.parseInt(pageSize);
            limitPageSize = Integer.parseInt(pageSize);
        }
        map.put("limitStartIndex", limitStartIndex);
        map.put("limitPageSize", limitPageSize);
        List<ImageInfo> imageList = cachedImageInfo.searchImage(map);
        resultData.put("list", imgListToJSONArray(imageList));

        JSONObject pageInfo = new JSONObject();
        int count = cachedImageInfo.countImage();
        if ((limitStartIndex + limitPageSize) < count) {
            pageInfo.put("isMore", true);
        } else {
            pageInfo.put("isMore", false);
        }
        pageInfo.put("pageNum", pageNum == null ? 1 : Integer.parseInt(pageNum) + 1);
        pageInfo.put("pageSize", limitPageSize);
        pageInfo.put("total", imageList == null ? 0 : imageList.size());
        resultData.put("pageInfo", pageInfo);
        return resultData;

    }

    private JSONArray imgListToJSONArray(List<ImageInfo> imageList) {
        if (CollectionUtils.isEmpty(imageList)) {
            return new JSONArray();
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = null;
        for (ImageInfo imageInfo : imageList) {
            jsonObj = new JSONObject();
            jsonObj.put("imageLabel", imageInfo.getImageLabel());
            jsonObj.put("imageName", imageInfo.getImageName());
            jsonObj.put("md5", imageInfo.getMd5());
            jsonObj.put("uploadTime",
                    DateUtil.d2s(
                            imageInfo.getUpdateTime() == null ? imageInfo.getCreateTime()
                                    : imageInfo.getUpdateTime(),
                            DateUtil.YYYYMMDDHHMMSSSSSWITHSYMBOL));
            jsonArray.add(jsonObj);
        }
        return jsonArray;
    }

}
