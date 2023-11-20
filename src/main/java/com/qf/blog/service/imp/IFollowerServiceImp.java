package com.qf.blog.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qf.blog.common.constants.FollowerConstants;
import com.qf.blog.common.constants.LikeConstatns;
import com.qf.blog.common.exception.BlogException;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.entity.UserEntity;
import com.qf.blog.service.IFollowerService;
import com.qf.blog.service.UserService;
import com.qf.blog.vo.FollowerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IFollowerServiceImp implements IFollowerService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserHelp userHelp;
    @Autowired(required = false)
    private FollowerVo followerVo;
    @Autowired(required = false)
    private UserEntity userEntity;
    @Autowired
    private UserService userService;

    @Override
    public void click(Integer fid, Integer uid) {
        //点击关注
        //点击关注 产生两个关系，你关注了对方 你成为对方粉丝


        if (userHelp == null) {
            throw new BlogException("用户未登录");
        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String fansKey = String.format(FollowerConstants.FANS_KEY, fid);//fans
                String key = String.format(FollowerConstants.FOLLOWER_KEY, uid);//关注
                //Boolean member = redisTemplate.opsForSet().isMember(fansKey,followerVo.getUserid());
                //查询是否关注过他
                Double time = redisTemplate.opsForZSet().score(key, fid);
                operations.multi();
                if (time != null) {
                    //已关注 则取消
                    redisTemplate.opsForZSet().remove(key, uid);
                    redisTemplate.opsForZSet().remove(fansKey, fid);
                    //被取消关注的用户粉丝数量减少
                    redisTemplate.opsForValue().decrement(key);

                } else {
                    //如果没有关注则进行增粉丝数
                    //存入关注信息 关注发生时间（后面查看列表按时间排序）
                    redisTemplate.opsForZSet().add(key, fid, System.currentTimeMillis());
                    //被关注用户 粉丝增加
                    redisTemplate.opsForZSet().add(fansKey, uid, System.currentTimeMillis());

                }
                //事务提交
                operations.exec();
                return null;
            }
        });

    }

    @Override
    public Boolean findUserFollowerStatus(Integer userId, Integer fid) {
        //返回 你是否关注过他
        String key = String.format(FollowerConstants.FOLLOWER_KEY, userId);
        Double time = redisTemplate.opsForZSet().score(key, fid);
        return time != null ? true : false;
    }

    @Override
    public Long findUserFollowerCount(Integer userId) {
        //统计关注数量
        String key = String.format(FollowerConstants.FOLLOWER_KEY, userId);//在被關注人中填入自己的key
        return redisTemplate.opsForZSet().size(key);
    }

    @Override
    public Long findUserFansUserCount(Integer userId) {
        //統計粉絲數量
        String fanskey = String.format(FollowerConstants.FANS_KEY, userId);//在自己内存中存入粉絲用戶的key


        return redisTemplate.opsForZSet().size(fanskey);
    }

    @Override
    public List<FollowerVo> findFollowerListByUserId(Integer userId, Integer type) {
        //創建一個列表存儲關注人的信息
        List<FollowerVo> list = new ArrayList<>();
        //redis中存入 用戶id
        //可以根據用戶id 把其他信息查出來
        String key = null;
        if (type == 1) {
            //查詢關注人的列表
            key = String.format(FollowerConstants.FOLLOWER_KEY, userId);

        } else {
            //查詢粉絲列表
            key = String.format(FollowerConstants.FANS_KEY, userId);
        }
        //查詢所有存儲結果
        Set set = redisTemplate.opsForZSet().range(key, 0, -1);
        //循環集合 通過用戶Id 查詢每一個用戶Id
        Iterator iterator = set.iterator();
        //判斷集合是否存在下一條記錄
        //如果存在下一條記錄 繼續循環 不存 終止信息
        while (iterator.hasNext()) {
            //拿到對方的id
            Integer uid = (Integer) iterator.next();
            //關注時間
            Double time = redisTemplate.opsForZSet().score(key, uid);
            //查詢用戶的信息
            UserEntity userEntity = userService.findUserById(uid);
            //把查詢到的用戶信息放入FollowerVo
            FollowerVo vo = new FollowerVo();
            vo.setUserid(userEntity.getId());
            vo.setUsername(userEntity.getUsername());
            vo.setCreateTime(new Date(time.longValue()));
            vo.setHeadUrl(userEntity.getHeadUrl());
            list.add(vo);
        }
        return list;
    }
}
