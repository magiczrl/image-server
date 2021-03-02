package com.cn.image.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @Desc 
 * @author Z.R.L
 * @date 2020年9月17日 下午2:19:57
 */
public class UserAccount implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5384209598160940990L;

    private int id;
    private String userName;
    private String passwd;
    private String userRole;
    private Date createTime;
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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
