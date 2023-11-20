package com.qf.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qf.blog.entity.UserEntity;
import com.qf.blog.vo.UserToken;

public interface UserService extends IService<UserEntity> {
    //注册业务接口
    void register(UserEntity userEntity);

    void sendEmail();

    //激活业务接口
    void activityUser(String code);

    //登录接口
    UserToken login(String username, String pwd);

    //退出登录接口
    void logout(String token);

    //修改密码
    void updatePassword(String oldPassword, String newPassword, String newPassword2, UserToken userToken);

    //查找用户信息
    UserEntity findUserById(Integer uid);
}
