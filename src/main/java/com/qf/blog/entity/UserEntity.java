package com.qf.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_user")
public class UserEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;//主键id
    private String username;// 用户名
    private String pwd;//密码
    private Integer sex;//性别
    private String mobile;//手机号
    private String email;//邮箱
    private Integer statu;//状态
    private String headUrl;//头像
    private String activateCode;//激活码
    private Integer score;//评分
    private Date createTime;//创建时间
}
