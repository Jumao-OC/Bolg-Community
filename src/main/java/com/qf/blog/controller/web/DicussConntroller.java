package com.qf.blog.controller.web;

import com.qf.blog.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DicussConntroller {
    @GetMapping("/user/discuss/{id}")
    public String getDiscuss(@PathVariable Integer id) {
        return "/site/discuss";
    }
}
