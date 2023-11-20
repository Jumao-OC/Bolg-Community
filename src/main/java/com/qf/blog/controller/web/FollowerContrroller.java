package com.qf.blog.controller.web;

import com.qf.blog.common.help.UserHelp;
import com.qf.blog.service.IFollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FollowerContrroller {
    @Autowired
    private IFollowerService iFollowerService;
    @Autowired
    private UserHelp userHelp;

    @GetMapping("/follower/findFollowerListByUserId/{uid}")
    public String findFollowerListByUserId(@PathVariable Integer uid, ModelMap modelMap) {
        //查询当前用户粉丝列表
        /*iFollowerService.findFollowerListByUserId(uid,);*/
        return "site/followee";
    }
}
