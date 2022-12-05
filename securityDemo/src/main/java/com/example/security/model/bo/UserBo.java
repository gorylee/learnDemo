package com.example.security.model.bo;

import lombok.Data;

/**
 * @Author GoryLee
 * @Date 2022/11/15 20:40
 * @Version 1.0
 */
@Data
public class UserBo {

    private Long id;

    private String userName;

    private String avatar;

    private String email;

    private String password;

    private Integer status;
}
