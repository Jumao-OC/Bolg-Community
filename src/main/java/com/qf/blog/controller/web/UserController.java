package com.qf.blog.controller.web;

import com.qf.blog.common.exception.BlogException;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.common.utils.R;
import com.qf.blog.entity.UserEntity;
import com.qf.blog.service.IFollowerService;
import com.qf.blog.service.ILikeService;
import com.qf.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class UserController {
    @Autowired
    private UserHelp userHelp;
    @Autowired
    private UserService userService;
    @Autowired
    private IFollowerService followerService;
    @Autowired
    private ILikeService likeService;

    @GetMapping("/info/{userId}")
    public String info(@PathVariable Integer userId, ModelMap modelMap) {

        if (ObjectUtils.isEmpty(userId)) {
            throw new BlogException("用户id不能为空");
        }
        //从数据库中查询用户信息
        UserEntity userEntity = userService.getById(userId);
        userEntity.setPwd(null);
        //查询用户的点赞信息
        Long userLikeCount = likeService.getUserLikeCount(userId);
        //查询当前用户是否关注了该用户
        Boolean hasFollowed = followerService.findUserFollowerStatus(userId, userHelp.get().getUid());
        if (userHelp.get() == null) {
            throw new BlogException("用户未登录");
        }
        //查询当前用户的关注总数
        Long followerCount = followerService.findUserFollowerCount(userHelp.get().getUid());
        //查询粉丝总数
        Long fansCount = followerService.findUserFansUserCount(userId);
        modelMap.put("user", userEntity);
        modelMap.put("userLikeCount", userLikeCount);
        modelMap.put("hasFollowed", hasFollowed);
        modelMap.put("followerCount", followerCount);
        modelMap.put("fansCount", fansCount);
        return "site/profile";
    }
}






