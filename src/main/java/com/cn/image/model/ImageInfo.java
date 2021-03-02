package com.cn.image.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc 
 * @author Z.R.L
 * @date 2020年9月22日 下午2:04:54
 */
public class ImageInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3880320485386045668L;

    private int id;
    private String imageName;
    private String imageLabel;
    private String md5;
    private Date createTime;
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageLabel() {
        return imageLabel;
    }

    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
