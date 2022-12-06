package com.example.security.model.bo;

import lombok.Data;

import java.sql.Blob;

/**
 * @Author GoryLee
 * @Date 2022/11/15 20:40
 * @Version 1.0
 */
@Data
public class UserBo {

    private Long id;

    private String email;

    private String userName;

    private String password;

    private String role;

    private String state;

    private String sex;

    private String source;

    private String avatar;

    private String signature;

    private Blob ext;
}
