package com.example.quartz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author GoryLee
 * @date  2023-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sa_customer_additional_services")
@ApiModel(value="客户-附加服务关联表", description="客户-附加服务关联表表实体")
public class CustomerAdditionalServices implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司附加服务ID")
    private Long companyAdditionalId;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "已删除1")
    private Integer deleted;

    @ApiModelProperty(value = "创建者ID")
    private Long creatorId;

    private Date createTime;

    @ApiModelProperty(value = "创建者")
    private String creatorName;

    @ApiModelProperty(value = "修改者ID")
    private Long modifierId;

    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "修改者")
    private String modifierName;


}
