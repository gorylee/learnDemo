package com.example.security.model.bo;

import lombok.Data;

import java.sql.Blob;

/**
 * @Description
 * @Author GorryLee
 * @Date 2022/12/6
 */
@Data
public class UserAddBo {

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
