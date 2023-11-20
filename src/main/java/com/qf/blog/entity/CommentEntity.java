package com.qf.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_comment")
public class CommentEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;//评论id
    private Integer uid;//用户id
    private Integer entityType;//实体类型(1 对帖子评论 2 对评论的回复)
    private Integer entityId;//实体id
    private Integer targetId;//目标id(被评论用户id)
    private String content;//评论内容
    private Integer statu;//评论状态
    private Date createTime;//创建时间
}
