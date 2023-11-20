package com.qf.blog.config;

import com.qf.blog.common.constants.UserConstants;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.common.utils.CookieUtils;
import com.qf.blog.vo.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Component
@Slf4j
public class LoginUserInterceprot implements HandlerInterceptor {//拦截器在进入控制层前面执行，相当于前端数据先进入拦截器再进入控制层接口
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserHelp userHelp;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从cookie中获取token
        String token = CookieUtils.getValue(request);
        //判断usertoken不可为空
        if (!ObjectUtils.isEmpty(token)) {
            UserToken userToken = (UserToken) redisTemplate.opsForValue().get(String.format(UserConstants.USER_LOGIN_TOKEN, token));//键值对类型，获取key键对应的值
            //判断usertoken不可为空且没有过期
            if (userToken != null && userToken.getTtl().after(new Date())) {//Date1.after(Date2),当Date1大于Date2时，返回TRUE，当小于等于时，返回false；
                userHelp.set(userToken);
            } else {
                log.debug("token过期或者redis中不存在该token");
            }
        }

        return true;
    }

    @Override//一般用来销毁资源，移除内存中的数据
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userHelp.remove();
    }

    @Override//视图解析之前进行逻辑视图转化为物理视图
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserToken userToken = userHelp.get();
        if (userToken != null) {
            if (modelAndView != null) {
                modelAndView.addObject("loginUser", userToken);

                /*

                 * 该方法相当于request.setAttribute，在页面上直接使用${attributeName}取值
                 * 如果方法的返回值是ModelAndView，在创建的html页面里,可以通过${attributeName}，
                 * 定位到attributeValue。
                 *
                 * */
            }
        }
    }
}


                /*
                视图解析器的作用是只对Controller类中每个函数返回值那里的String类型前后加路径，
                而不对@RequestMapping处加路径，对@RequestMapping处无影响！
                */
