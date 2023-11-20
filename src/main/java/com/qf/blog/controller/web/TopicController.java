package com.qf.blog.controller.web;

import com.qf.blog.common.dto.BlogPage;
import com.qf.blog.common.exception.BlogException;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.common.utils.R;
import com.qf.blog.entity.InvitationEntity;
import com.qf.blog.entity.UserEntity;
import com.qf.blog.service.CommentService;
import com.qf.blog.service.ILikeService;
import com.qf.blog.service.InvitationService;
import com.qf.blog.service.UserService;
import com.qf.blog.vo.CommentVo;
import com.qf.blog.vo.LikeVo;
import com.qf.blog.vo.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private UserHelp userHelp;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ILikeService iLikeService;

    //新增发帖子接口
    @PostMapping("/publish")
    @ResponseBody
    public R publish(@RequestBody InvitationEntity invitationEntity) {
        //获取用户信息 从Redis中
        UserToken userToken = userHelp.get();
        if (userToken == null) {
            throw new BlogException("用户没有登录");
        }
        invitationEntity.setStatu(1);
        invitationEntity.setCreateTime(new Date());
        invitationEntity.setUid(userToken.getUid());
        invitationService.save(invitationEntity);
        return R.ok();
    }

    //帖子详情接口
    @GetMapping("/detail/{id}")
    public String detail(BlogPage<CommentVo> page, @PathVariable Integer id, ModelMap modelMap) {
        //查看帖子信息
        InvitationEntity topic = invitationService.getById(id);
        //查看帖子作者信息
        UserEntity user = userService.getById(topic.getUid());

        //设置帖子数据
        page.setSize(5);
        //帖子评论 查询帖子评论 返回前端
        page = commentService.getCommentListByTid(page, id);
        //帖子点赞
        LikeVo likeVo = new LikeVo();
        likeVo.setEntityType(1);
        likeVo.setEntityId(id);

        if (userHelp.get() != null) {
            likeVo.setUserId(userHelp.get().getUid());
        }
        //查询实体点赞数量
        Long likeCount = iLikeService.LikeCount(likeVo);
        //当前用户对该帖子的点赞状态
        Boolean likeStatus = iLikeService.likeStatus(likeVo);
        //设置page路径
        page.setPath("/topic/detail" + id);
        modelMap.addAttribute("topic", topic);
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("likeCount", likeCount);
        modelMap.addAttribute("likeStatus", likeStatus ? 1 : 0);
        return "site/discuss-detail";
    }

}
