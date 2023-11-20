package com.qf.blog.controller.web;

import com.qf.blog.common.exception.BlogException;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.common.utils.R;
import com.qf.blog.service.IFollowerService;
import com.qf.blog.service.ILikeService;
import com.qf.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/follower")
public class IFollowerController {
    @Autowired
    private UserHelp userHelp;
    @Autowired
    private UserService userService;
    @Autowired
    private IFollowerService iFollowerService;
    @Autowired
    private ILikeService iLikeService;

    @GetMapping("/click/{uid}")
    public R click(@PathVariable Integer uid) {
        if (userHelp.get() == null) {
            throw new BlogException("请登录");
        }
        int fid = userHelp.get().getUid();
        iFollowerService.click(fid, uid);

        return R.ok();
    }
}
