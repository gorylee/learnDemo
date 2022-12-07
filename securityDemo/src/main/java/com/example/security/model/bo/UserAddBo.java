package com.example.security.model.bo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description
 * @Author GorryLee
 * @Date 2022/12/6
 */
@Data
public class UserAddBo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 登录账号
     */
    private String loginName;


    /**
     * 用户昵称
     */
    private String userName;


    /**
     * 用户类型（1系统用户 0注册用户）
     */
    private String userType;


    /**
     * 用户邮箱
     */
    private String email;


    /**
     * 手机号码
     */
    private String phoneNumber;


    /**
     * 用户性别（0男 1女 2未知）
     */
    private String sex;


    /**
     * 头像路径
     */
    private String avatar;


    /**
     * 密码
     */
    private String password;


    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;


    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;


    /**
     * 创建者
     */
    private String createBy;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    /**
     * 更新者
     */
    private String updateBy;


    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    /**
     * 备注
     */
    private String remark;

}
