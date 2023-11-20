package com.qf.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qf.blog.common.dto.BlogPage;
import com.qf.blog.entity.CommentEntity;
import com.qf.blog.vo.CommentVo;

public interface CommentService extends IService<CommentEntity> {
    //查询评论表的数据，包括评论人和被评论人的数据
    BlogPage<CommentVo> getCommentListByTid(BlogPage<CommentVo> page, Integer id);

    //查询评论数量
    Long getCommentCountByTopicId(Integer type, Integer id);
}
