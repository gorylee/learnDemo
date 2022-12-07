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
@TableName("sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 角色ID
    */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
    * 角色名称
    */
    private String roleName;

    /**
    * 角色权限字符串
    */
    private String roleKey;

    /**
    * 显示顺序
    */
    private Integer roleSort;

    /**
    * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
    */
    private String dataScope;

    /**
    * 角色状态（0正常 1停用）
    */
    private String status;

    /**
    * 删除标志（0代表存在 2代表删除）
    */
    private String delFlag;

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
