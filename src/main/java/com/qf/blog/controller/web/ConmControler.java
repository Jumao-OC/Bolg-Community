package com.qf.blog.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConmControler {
    @GetMapping("/letter/list")
    public String getCon() {
        return "site/conmi";
    }
}
