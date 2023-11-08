package com.example.xxjob.service;

import org.thymeleaf.context.Context;

/**
 * @Description
 * @Author GoryLee
 * @Date 2023/11/8
 */
public interface EmailService {

    void sendSimpleMail(String from, String to, String subject, String text);

    Boolean sendMimeMail(String from, String to, String subject, String text);

    Boolean sendTemplateMail(String from, String to, String subject, Context context);
}
