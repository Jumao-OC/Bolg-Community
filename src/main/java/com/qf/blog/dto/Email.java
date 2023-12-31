package com.qf.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String title;//邮件标题
    private String content;//邮件内容
    private String toUser;//收件人
}
