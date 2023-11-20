package com.qf.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.blog.common.dto.BlogPage;
import com.qf.blog.entity.CommentEntity;
import com.qf.blog.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentMapper extends BaseMapper<CommentEntity> {
    //获取评论列表
    BlogPage<CommentVo> selectCommentListTid(@Param("page") BlogPage<CommentVo> page
            , @Param("tid") Integer id
            , @Param("type") Integer type);

    //根据条件返回评论的数量
    Long getCommentCountByTopicId(@Param("type") Integer type,
                                  @Param("id") Integer id);

}
