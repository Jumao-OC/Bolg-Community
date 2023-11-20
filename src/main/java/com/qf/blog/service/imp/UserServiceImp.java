package com.qf.blog.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qf.blog.common.constants.UserConstants;
import com.qf.blog.common.exception.BlogException;
import com.qf.blog.common.exception.EmailException;
import com.qf.blog.common.executor.ExecutorUtils;
import com.qf.blog.common.status.BlogStatus;
import com.qf.blog.common.utils.EmailUtils;
import com.qf.blog.common.utils.PasswordUtils;
import com.qf.blog.common.utils.UUIDUtils;
import com.qf.blog.dto.Email;
import com.qf.blog.entity.UserEntity;
import com.qf.blog.mapper.UserMapper;
import com.qf.blog.service.UserService;
import com.qf.blog.vo.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service("userService")

public class UserServiceImp extends ServiceImpl<UserMapper, UserEntity> implements UserService {


    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void register(UserEntity userEntity) {
        //判断当前邮箱是否被注册
        QueryWrapper queryWrapper = new QueryWrapper();
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper.eq("email", userEntity.getEmail());
        queryWrapper1.eq("username", userEntity.getUsername());
        UserEntity dbUserEntityEmail = baseMapper.selectOne(queryWrapper);
        int dbUserEntityName = baseMapper.selectCount(queryWrapper1);
        if (dbUserEntityEmail != null || dbUserEntityName > 1) {//如果不为空则已被注册
            throw new EmailException("该邮箱已被注册或用户名重复！");
        }
        //设置用户信息以及邮箱状态未激活
        userEntity.setStatu(BlogStatus.USER_UN_ACTIVATE.getCode());
        userEntity.setActivateCode(UUIDUtils.getUUid());//用户点击完注册之后赋予一个激活码
        userEntity.setCreateTime(new Date());
        userEntity.setPwd(PasswordUtils.encode(userEntity.getPwd()));
        baseMapper.insert(userEntity);
        //将激活码存入内存
        stringRedisTemplate.opsForValue().set(
                String.format(UserConstants.ACTIVATEKEY, userEntity.getActivateCode()),
                userEntity.getId().toString(),
                1,
                TimeUnit.DAYS
        );
        Email email = new Email();
        email.setTitle("这是Blog网站用户激活邮件");
        email.setToUser(userEntity.getEmail());
        email.setContent("<a href ='http://127.0.0.1:8001/user/activateUser/"
                + userEntity.getActivateCode() + "'>点击</a>这里激活");
        ExecutorUtils.getExecutor().submit(() -> {
            emailUtils.sendEmail(email);
        });

    }

    @Override
    public void sendEmail() {

    }

    @Override
    public void activityUser(String code) {
        //从redis（内存）中找到用户激活码
        String userId = stringRedisTemplate.opsForValue().get(String.format(UserConstants.ACTIVATEKEY, code));//根据激活码寻找用户id（redis中）
        if (ObjectUtils.isEmpty(userId)) {
            throw new BlogException("激活码错误");
        }
        Integer i = baseMapper.updateUserStatus(BlogStatus.USER_ACTIVATE.getCode(), userId, code);
        stringRedisTemplate.delete(String.format(UserConstants.ACTIVATEKEY, code));
    }

    @Override
    public UserToken login(String username, String pwd) {
        //根据用户名查询用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);//对传过来的数据进行封装，username = “李四”
        //使用myBplus创建查询语句，对数据库进行查询
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        if (userEntity == null) {
            throw new BlogException("用户不存在");
        }
        if (!PasswordUtils.checkpw(pwd, userEntity.getPwd())) {
            throw new BlogException("密码校验失败");
        }
        if (userEntity.getStatu() != BlogStatus.USER_ACTIVATE.getCode()) {
            throw new BlogException("用户未激活");
        }
        UserToken userToken = new UserToken();
        userToken.setCreateTime(new Date());
        userToken.setUid(userEntity.getId());
        userToken.setUname(userEntity.getUsername());
        userToken.setHeaderUrl(userEntity.getHeadUrl());
        userToken.setToken(UUIDUtils.getUUid());
        long currentTime = System.currentTimeMillis();
        //生成用户默认超时时间
        long userDefaultTimeOut = UserConstants.USER_DEFAULE_TIMEOUT * 1000;
        long userTtl = currentTime + userDefaultTimeOut;
        Date ttl = new Date(userTtl);
        userToken.setTtl(ttl);
        String key = String.format(UserConstants.USER_LOGIN_TOKEN, userToken.getToken());
        redisTemplate.opsForValue().set(key, userToken, userDefaultTimeOut, TimeUnit.MICROSECONDS);
        return userToken;
    }

    @Override
    public void logout(String token) {
        //删除redis
        String key = String.format(UserConstants.USER_LOGIN_TOKEN, token);
        redisTemplate.delete(key);
    }

    //修改密码
    @Override
    public void updatePassword(String oldPassword, String newPassword, String newPassword2, UserToken userToken) {
        //判断上面的变量不为空
        if (oldPassword == null
                || oldPassword.equals("")
                || newPassword == null
                || newPassword.equals("")
                || newPassword2 == null
                || newPassword2.equals("")) {
            throw new BlogException("参数错误");
        }
        //新密码和确认密码一致
        if (!newPassword2.equals(newPassword)) {
            throw new BlogException("两次密码不一致");
        }
        //从数据库中查询用户的完整信息（）
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userToken.getUid());
        UserEntity userEntity = baseMapper.selectOne(queryWrapper);
        //判断查询结果是否为空
        if (userEntity == null) {
            throw new BlogException("查询结果为空");
        }
        //旧密码和数据库密码比对
        if (!PasswordUtils.checkpw(oldPassword, userEntity.getPwd())) {
            throw new BlogException("密码错误");
        }
        //修改数据库中的密码
        userEntity.setPwd(PasswordUtils.encode(newPassword));
        Integer integer = baseMapper.updateById(userEntity);
        //integer表示修改数据条数
        if (integer < 0) {
            throw new BlogException("密码查询失败");
        }

        //退出登录
        String key = String.format(UserConstants.USER_LOGIN_TOKEN, userToken.getToken());
        redisTemplate.delete(key);
        //返回修改成功
    }

    @Override
    public UserEntity findUserById(Integer uid) {
        //去Redis查询用户信息
        String key = String.format(UserConstants.USER_INFO_KEY, uid);
        UserEntity userEntity = (UserEntity) redisTemplate.opsForValue().get(key);
        //若redis中没有该信息
        if (userEntity == null) {
            Lock lock = new ReentrantLock();//创建一个锁
            try {
                //尝试拿到锁（资源）
                boolean b = lock.tryLock();
                if (b) {
                    //如果拿到资源 业务只有一个人能通过
                    userEntity = getById(uid);
                    if (userEntity == null) {
                        redisTemplate.opsForValue().set(key, null, 1, TimeUnit.DAYS);
                    } else {
                        redisTemplate.opsForValue().set(key, userEntity, 1, TimeUnit.DAYS);
                    }
                } else {
                    //如果没有拿到资源呢
                    //等待0.5秒
                    Thread.sleep(500);
                    return findUserById(uid);//自旋
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //解锁
                lock.unlock();
            }
        }
        // 去mysql查询 将结果存在redis
        return userEntity;
    }


}

