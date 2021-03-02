package com.cn.image.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.image.common.ActionResponse;
import com.cn.image.common.BizException;
import com.cn.image.common.ResMsg;
import com.cn.image.interceptor.Auth;
import com.cn.image.service.ImageService;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@RestController
public class ImageController {

    private static Logger logger = LoggerFactory.getLogger(ImageController.class);
    @Autowired
    private ImageService imageService;

    /**
     * 上传图片接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/img/upload" }, produces = { "application/json;charset=UTF-8" })
    @Auth
    public ActionResponse upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        ActionResponse ar = new ActionResponse();
        try {
            ar.setData(imageService.upload(file, "1"));
            ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                    ResMsg.SUCCESS.getReturnMessage());
            return ar;
        } catch (BizException e) {
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(e.getReturnCode(), e.getReturnMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(ResMsg.FAIL.getReturnCode(), "system error");
        }
        return ar;
    }

    /**
     * hjb-osp上传banner
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/img/uploadInnerForward" }, produces = {
            "application/json;charset=UTF-8" })
    public ActionResponse uploadInnerForward(@RequestParam(value = "file", required = false) MultipartFile file) {
        ActionResponse ar = new ActionResponse();
        try {
            ar.setData(imageService.upload(file, "banner"));
            ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                    ResMsg.SUCCESS.getReturnMessage());
            return ar;
        } catch (BizException e) {
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(e.getReturnCode(), e.getReturnMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(ResMsg.FAIL.getReturnCode(), "system error");
        }
        return ar;
    }

    /**
     * 获取图片列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/img/list" }, produces = { "application/json;charset=UTF-8" })
    @Auth
    public ActionResponse imgList(@RequestParam(required = false) String imageName,
                                  @RequestParam(required = false) String imageLabel,
                                  @RequestParam(required = false) String pageNum,
                                  @RequestParam(required = false) String pageSize) {
        ActionResponse ar = new ActionResponse();
        try {
            ar.setData(imageService.imgList(imageName, imageLabel, pageNum, pageSize));
            ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                    ResMsg.SUCCESS.getReturnMessage());
            return ar;
        } catch (BizException e) {
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(e.getReturnCode(), e.getReturnMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(ResMsg.FAIL.getReturnCode(), "system error");
        }
        return ar;
    }

    /**
     * 获取所有的图片自定义类型
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/img/getAllImageLabel" }, produces = {
            "application/json;charset=UTF-8" })
    @Auth
    public ActionResponse findAllImageLabel() {
        ActionResponse ar = new ActionResponse();
        try {
            ar.setData(imageService.getAllImageLabel());
            ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                    ResMsg.SUCCESS.getReturnMessage());
            return ar;
        } catch (BizException e) {
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(e.getReturnCode(), e.getReturnMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            ar.setData(new JSONObject());
            ar.setReturnCodeAndMessage(ResMsg.FAIL.getReturnCode(), "system error");
        }
        return ar;
    }

    /**
     * 通过md5删除图片
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/img/deleteByMd5" }, produces = { "application/json;charset=UTF-8" })
    @Auth
    public ActionResponse deleteByMd5(@RequestBody JSONObject md5Json) {
        ActionResponse ar = new ActionResponse();
        ar.setData(new JSONObject());
        try {
            JSONArray md5Array = md5Json.getJSONArray("md5");
            imageService.deleteByMd5(md5Array);
            ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                    ResMsg.SUCCESS.getReturnMessage());
            return ar;
        } catch (BizException e) {
            ar.setReturnCodeAndMessage(e.getReturnCode(), e.getReturnMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            ar.setReturnCodeAndMessage(ResMsg.FAIL.getReturnCode(), "system error");
        }
        return ar;
    }

    /**
     * 更新图片信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/img/updateImageInfo" }, produces = {
            "application/json;charset=UTF-8" })
    @Auth
    public ActionResponse updateImageInfo(@RequestBody JSONObject requestJson) {
        ActionResponse ar = new ActionResponse();
        ar.setData(new JSONObject());
        try {
            String imageLabel = requestJson.getString("imageLabel");
            String imageName = requestJson.getString("imageName");
            String md5 = requestJson.getString("md5");
            imageService.updateImageInfo(imageLabel, imageName, md5);
            ar.setReturnCodeAndMessage(ResMsg.SUCCESS.getReturnCode(),
                    ResMsg.SUCCESS.getReturnMessage());
            return ar;
        } catch (BizException e) {
            ar.setReturnCodeAndMessage(e.getReturnCode(), e.getReturnMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            ar.setReturnCodeAndMessage(ResMsg.FAIL.getReturnCode(), "system error");
        }
        return ar;
    }

    /**
     * 浏览图片：响应图片流
     *   无需认证
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/browserImage/{md5}" }, produces = { MediaType.IMAGE_JPEG_VALUE,
            /*MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE*/ })
    @ResponseBody
    public byte[] browserImageByMd5(@PathVariable String md5) {
        try {
            return imageService.browserImage(md5, null);
        } catch (BizException e) {
            return null;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 浏览图片：响应图片流
     *   无需认证
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = { "/browserImage/getUrlFromImageName/{imageName}" }, produces = {
            MediaType.IMAGE_JPEG_VALUE,
            /*MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE*/ })
    @ResponseBody
    public byte[] browserImageByImageName(@PathVariable String imageName) {
        try {
            return imageService.browserImage(null, imageName);
        } catch (BizException e) {
            return null;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

}
