package com.qf.blog;

import com.qf.blog.entity.InvitationEntity;
import com.qf.blog.entity.UserEntity;
import com.qf.blog.mapper.InvitationMapper;
import com.qf.blog.mapper.UserMapper;
import com.qf.blog.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private InvitationMapper invitationMapper;


    @Test
    public String P() {
        Boolean abc = false;
        String a;
        if (abc) {
            abc = true;
            a = "1";
        } else {
            abc = false;
            a = "2";
        }
        return a;
    }


}
