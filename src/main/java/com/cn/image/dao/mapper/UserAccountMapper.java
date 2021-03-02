package com.cn.image.dao.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cn.image.model.UserAccount;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月17日 下午2:19:01
 */
@Mapper
public interface UserAccountMapper {

    /**
     * 
     * @param userName
     * @return
     */
    public UserAccount findUserAccountByUserName(String userName);
}
