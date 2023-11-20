package com.qf.blog.service;

import com.qf.blog.vo.LikeVo;

public interface ILikeService {
    //点赞
    public void clickLike(LikeVo likeVo);

    //查询点赞数量
    Long LikeCount(LikeVo likeVo);

    //查询点赞状态
    Boolean likeStatus(LikeVo likeVo);

    //查询用户点赞数量
    Long getUserLikeCount(Integer userId);
}
