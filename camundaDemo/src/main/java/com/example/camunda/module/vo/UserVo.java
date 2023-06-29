package com.example.camunda.module.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
* @author GoryLee
* @date  2023-6-29
*/
@Data
@Accessors(chain = true)
@ApiModel(value="用户信息表")
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "用户权限：1公司，2事业部，3集团")
    private String userRole;


}
