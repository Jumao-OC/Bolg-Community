package com.qf.blog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FollowerVo {
    private Integer userid;
    private String username;
    private Date createTime;
    private String headUrl;

}
