package com.qf.blog.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserToken implements Serializable {
    private Integer uid;
    private String uname;
    private Date ttl;//过期时间
    private Date createTime;//登录时间
    private String token;//令牌，令牌给用户用来做校验
    private String headerUrl;
}
