package com.qf.blog.service;

import com.qf.blog.vo.FollowerVo;

import java.util.List;

public interface IFollowerService {
    //关注 取消关注
    void click(Integer fid, Integer uid);

    //查看关注状态
    Boolean findUserFollowerStatus(Integer userId, Integer fid);

    //查看你关注的人总数
    Long findUserFollowerCount(Integer userId);

    //查看你粉丝的数量
    Long findUserFansUserCount(Integer userId);

    //查看关注列表
    List<FollowerVo> findFollowerListByUserId(Integer userId, Integer type);
}
