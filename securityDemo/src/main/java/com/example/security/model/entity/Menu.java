package com.example.security.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author GorryLee
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 菜单ID
    */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    /**
    * 菜单名称
    */
    private String menuName;

    /**
    * 父菜单ID
    */
    private Long parentId;

    /**
    * 显示顺序
    */
    private Integer orderNum;

    /**
    * 请求地址
    */
    private String url;

    /**
    * 打开方式（menuItem页签 menuBlank新窗口）
    */
    private String target;

    /**
    * 菜单类型（M目录 C菜单 F按钮）
    */
    private String menuType;

    /**
    * 菜单状态（0显示 1隐藏）
    */
    private String visible;

    /**
    * 是否刷新（0刷新 1不刷新）
    */
    private String isRefresh;

    /**
    * 权限标识
    */
    private String perms;

    /**
    * 菜单图标
    */
    private String icon;

    /**
    * 创建者
    */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
    * 更新者
    */
    @TableField(fill = FieldFill.INSERT)
    private String updateBy;

    /**
    * 更新时间
    */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime updateTime;

    /**
    * 备注
    */
    private String remark;


}
