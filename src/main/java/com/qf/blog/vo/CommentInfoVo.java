package com.qf.blog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CommentInfoVo {
    private Integer id;//评论id
    private Integer uid;//用户id
    private Integer entityType;//
    private Integer entityId;//可对帖子评论，对评论进行评论
    private Integer targetId;//目标id
    private String targetName;//目标名字
    private String content;//评论内容
    private Integer statu;//评论状态
    private Date createTime;//
    //评论数据VO
}
