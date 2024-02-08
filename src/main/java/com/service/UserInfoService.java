package com.service;

import com.entity.UserInfo;
import com.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *

 */
@Service
@Transactional
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**查询用户信息*/
    public UserInfo LookUserinfo(String userid) {
        return userInfoMapper.lookUserinfo(userid);
    }
    /**分页查询不同角色用户信息*/
    public List<UserInfo> queryAllUserInfo(Integer page,Integer count,Integer roleid,Integer userstatus){
        return userInfoMapper.queryAllUserInfo(page,count,roleid,userstatus);
    }
    /**查看不同角色用户总数*/
    public Integer queryAllUserCount(Integer roleid){
        return userInfoMapper.queryAllUserCount(roleid);
    }
    /**添加用户信息*/
    public Integer userReg(UserInfo userInfo){
        return userInfoMapper.userReg(userInfo);
    }
    /**修改用户信息*/
    public Integer UpdateUserInfo(UserInfo userInfo){
        return userInfoMapper.updateUserInfo(userInfo);
    }
    /**查询用户的昵称和头像**/
    public UserInfo queryPartInfo(String userid){
        return userInfoMapper.queryPartInfo(userid);
    }

    /**
     * 1. 获得现有积分
     * 2. 增加积分
     * */
    public Integer updatePoint(String userId) {
        UserInfo userInfo = userInfoMapper.lookUserinfo(userId);
        userInfo.setPoint(userInfo.getPoint()+1);
        Integer integer = userInfoMapper.updateUserInfo(userInfo);
        return integer;
    }
}
