package com.qf.blog.controller.web;

import com.qf.blog.common.exception.BlogException;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.common.utils.R;
import com.qf.blog.service.ILikeService;
import com.qf.blog.vo.LikeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private UserHelp userHelp;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ILikeService iLikeService;

    @PostMapping("/clickLike")
    public R clickLike(@RequestBody LikeVo likeVo, ModelMap modelMap) {
        if (userHelp.get() == null) {
            throw new BlogException("用户未登录");
        }
        //点赞
        likeVo.setUserId(userHelp.get().getUid());
        iLikeService.clickLike(likeVo);

        //统计点赞数量（帖子  回复）


        iLikeService.getUserLikeCount(likeVo.getUserId());
        Long likeCount = iLikeService.LikeCount(likeVo);
        //查询点赞状态
        Boolean likeStatus = iLikeService.likeStatus(likeVo);
        //数据返回给前端

        return R.ok().put("likeStatus", likeStatus ? 1 : 0).put("likeCount"
                , likeCount);
    }
}
