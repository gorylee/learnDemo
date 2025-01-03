package com.example.xxjob.controller;

import com.example.xxjob.service.EmailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/11/8
 */
@RestController
public class EmailController {
    @Resource
    private EmailService emailService;

    @RequestMapping("/sendSimpleMail")
    public String sendSimpleMail(){
        String from = "localhost777@163.com";
        String to = "1363222638@163.com";
        String subject = "这是简单邮件的标题";
        String text = "这是简单邮件的内容";
        emailService.sendSimpleMail(from, to, subject, text);
        return "成功";
    }

    @RequestMapping("/sendMimeMail")
    public Boolean sendMimeMail(){
        String from = "localhost777@163.com";
        String to = "13631450638@163.com";
        String subject = "这是附件邮件的标题";
        String text = "这是附件邮件的内容";
        return emailService.sendMimeMail(from, to, subject, text);
    }

    @RequestMapping("/sendTemplateMail")
    public Boolean sendTemplateMail(){
        String from = "localhost777@163.com";
        String to = "13631450638@163.com";
        String subject = "这是模板邮件的标题";
        String message = "详情：您正在尝试进行登录操作，若非是您本人的行为，请忽略!";
        String code = "123456789";
        Context context = new Context();
        context.setVariable("customerName", "广东大发发有限公司");
        context.setVariable("invoiceNo", "123456798");
        context.setVariable("invoiceAmount", "85963");
        context.setVariable("invoiceDate", "2023年8月9日");
        context.setVariable("invoiceUrl", "https://youliao.yuque.com/os7ke5/xmyii6/ldh3ud1rrgvxo6pw");
        return emailService.sendTemplateMail(from, to, subject, context );
    }
}
