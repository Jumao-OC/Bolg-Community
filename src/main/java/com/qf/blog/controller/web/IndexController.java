package com.qf.blog.controller.web;

import com.qf.blog.common.dto.BlogPage;
import com.qf.blog.service.InvitationService;
import com.qf.blog.vo.IndexDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    private InvitationService invitationService;

    @GetMapping("/")
    public String index(BlogPage<IndexDataVo> blogPage, ModelMap modelMap) {
        //设置一页数据条数
        blogPage.setSize(5);
        blogPage = (BlogPage) invitationService.indexPage(blogPage);
        blogPage.setPath("/");
        modelMap.put("page", blogPage);
        return "index";
    }
}
