package com.example.camunda.module.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
* @author GoryLee
* @date:  2023-6-29
*/
@Data
@Accessors(chain = true)
@ApiModel(value="用户信息表查询UserQueryBo")
public class UserQueryBo extends BaseQueryBo{


    @ApiModelProperty(value = "用户ID")
    private Long id;


    @ApiModelProperty(value = "用户昵称")
    private String userName;


    @ApiModelProperty(value = "用户权限：1公司，2事业部，3集团")
    private String userRole;

}
