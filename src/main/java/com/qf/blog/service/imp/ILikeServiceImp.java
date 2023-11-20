package com.qf.blog.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qf.blog.common.constants.LikeConstatns;
import com.qf.blog.service.ILikeService;
import com.qf.blog.vo.LikeVo;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class ILikeServiceImp implements ILikeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void clickLike(LikeVo likeVo) {
        //点赞 取消点赞
        ///redis操作频繁 放在一个事务中操作
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //对回复 帖子点赞
                String key = String.format(LikeConstatns.LIKE_TOPIC_KEY, likeVo.getEntityType(), likeVo.getEntityId());
                //对人点赞
                String userLikeKey = String.format(LikeConstatns.USER_LIKE_KEY, likeVo.getEntityUserId());

                //判断这个用户是否给这个对象点赞
                Boolean member = redisTemplate.opsForSet().isMember(key, likeVo.getUserId());
                //开启事务 （要么执行完事务 要么不去执行）
                operations.multi();
                if (member) {
                    //已点赞 则取消
                    Long remove = redisTemplate.opsForSet().remove(key, likeVo.getUserId());
                    //被取消点赞的用户点赞数量减少
                    redisTemplate.opsForValue().decrement(userLikeKey);

                } else {
                    //如果没有点赞则进行增加点赞
                    Long add = redisTemplate.opsForSet().add(key, likeVo.getUserId());
                    //被点赞用户 点赞增加
                    redisTemplate.opsForValue().increment(userLikeKey);

                }
                //事务提交
                operations.exec();

                return null;
            }
        });

    }

    @Override
    public Long LikeCount(LikeVo likeVo) {
        //统计（帖子 回复）点赞数量
        String key = String.format(LikeConstatns.LIKE_TOPIC_KEY, likeVo.getEntityType(), likeVo.getEntityId());
        return redisTemplate.opsForSet().size(key);
    }

    @Override
    public Boolean likeStatus(LikeVo likeVo) {

        String key = String.format(LikeConstatns.LIKE_TOPIC_KEY, likeVo.getEntityType(), likeVo.getEntityId());
        return redisTemplate.opsForSet().isMember(key, likeVo.getUserId());
    }

    @Override
    public Long getUserLikeCount(Integer userId) {
        //统计用户点赞数量
        String userLikeKey = String.format(LikeConstatns.USER_LIKE_KEY, userId);
        Object o = redisTemplate.opsForValue().get(userLikeKey);
        if (o == null) {
            return 0L;
        }
        return Long.parseLong(o.toString());
    }
}
