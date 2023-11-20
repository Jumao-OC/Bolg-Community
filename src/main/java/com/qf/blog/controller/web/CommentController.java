package com.qf.blog.controller.web;

import com.qf.blog.common.exception.BlogException;
import com.qf.blog.common.help.UserHelp;
import com.qf.blog.entity.CommentEntity;
import com.qf.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@Slf4j
@RequestMapping("/comment")
public class CommentController {
    //创建对象
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserHelp userHelp;
    @Value("${blog.host}")
    private String host;

    @PostMapping("/add/{topicId}")
    public String add(@PathVariable Integer topicId, CommentEntity commentEntity) {
        if (userHelp.get() == null) {
            throw new BlogException("用户未登录");
        }
        commentEntity.setUid(userHelp.get().getUid());
        commentEntity.setStatu(1);
        commentEntity.setCreateTime(new Date());
        commentService.save(commentEntity);
        return "redirect:" + host + "topic/detail/" + topicId;
    }

}
