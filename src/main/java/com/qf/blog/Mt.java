package com.qf.blog;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class Mt {
    public static void main(String[] args) {
        // QQ邮箱举例子
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("你的邮箱1");
        mailSender.setPassword("这里填授权码");
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(25);
        mailSender.setProtocol("smtp");

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("你的邮箱1");

        msg.setSubject("邮件标题");
        msg.setText("邮件内容");
        msg.setTo("你需要发送的邮箱账户，可以发给自己");

        try {
            mailSender.send(msg);
        } catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }


}
