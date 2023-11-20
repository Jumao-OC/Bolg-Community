package com.qf.blog.controller.web;

import com.qf.blog.common.constants.UserConstants;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.common.utils.R;
import com.qf.blog.entity.UserEntity;
import com.qf.blog.service.UserService;
import com.qf.blog.vo.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserHelp userHelp;


    @PostMapping("/password")
    public R password(String oldPassword, String newPassword, String newPassword2, HttpServletResponse response) {
        //获取用户的基本信息
        UserToken userToken = userHelp.get();
        //更新密码
        userService.updatePassword(oldPassword, newPassword, newPassword2, userToken);
        //删除浏览器token
        Cookie cookie = new Cookie(UserConstants.TOKEN_COOKIE_KEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return R.ok();
    }

    //增加一个退出登录接口
    @GetMapping("/logout")
    public R logout(@CookieValue(name = UserConstants.TOKEN_COOKIE_KEY, defaultValue = "") String token, HttpServletResponse response) {
        //删除redis中对应的用户信息
        userService.logout(token);
        //把浏览器上的token也对应删除
        Cookie cookie = new Cookie(UserConstants.TOKEN_COOKIE_KEY, "");//赋予浏览器token空值达到删除浏览器的目的
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return R.ok();
        /*

        因此cookie.setPath("/");之后，可以在webapp文件夹下的所有应用共享cookie，而cookie.setPath("/webapp_b/");是指cas应用设置的cookie只能在webapp_b应用下的获得，
        即便是产生这个cookie的cas应用也不可以。

        */
    }

    @PostMapping("/login")
    public R login(@RequestBody UserEntity userEntity, HttpServletResponse response) {
        UserToken userToken = userService.login(userEntity.getUsername(), userEntity.getPwd());
        Cookie cookie = new Cookie(UserConstants.TOKEN_COOKIE_KEY, userToken.getToken());
        cookie.setPath("/");
        cookie.setMaxAge(UserConstants.USER_DEFAULE_TIMEOUT);
        response.addCookie(cookie);
        return R.ok();


    }


    @PostMapping("/register")
    public R register(@RequestBody UserEntity userEntity) {
        userService.register(userEntity);
        return R.ok();
    }

    @GetMapping("/activateUser/{code}")
    public String activateUser(@PathVariable String code) {
        if (!ObjectUtils.isEmpty(code)) {
            userService.activityUser(code);
        }
        return null;
    }

}
