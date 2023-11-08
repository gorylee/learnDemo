package com.example.xxjob.service.impl;

import com.example.xxjob.service.EmailService;
import example.common.utils.XFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/11/8
 */
@Service("emailService")
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendSimpleMail(String from, String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 发件人
        simpleMailMessage.setFrom(from);
        // 收件人
        simpleMailMessage.setTo(to);
        // 邮件主题
        simpleMailMessage.setSubject(subject);
        // 邮件内容
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public Boolean sendMimeMail(String from, String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String url = "https://www.w3school.com.cn/example/xmle/note.xml";
        String filePath = "D:\\workFile";
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);

            String fileName = XFileUtils.downLoadByUrl(url, filePath, null);
            String fullFilePath = filePath + File.separator + fileName;
            File file = new File(fullFilePath);
            if (!file.exists()) {
                return false;
            }
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            helper.addAttachment(fileName, fileSystemResource);
            javaMailSender.send(mimeMessage);
            file.delete();
        } catch (Exception e) {
            log.error("发送复杂邮箱失败",e);
            return false;
        }
        return true;
    }

    @Override
    public Boolean sendTemplateMail(String from, String to, String subject, Context context) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            String mail = templateEngine.process("mailtemplate.html", context);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(mail,true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("发送模板邮箱失败",e);
            return false;
        }
        return true;
    }
}
