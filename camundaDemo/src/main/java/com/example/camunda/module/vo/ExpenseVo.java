package com.example.camunda.module.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* @author GoryLee
* @date  2023-6-29
*/
@Data
@Accessors(chain = true)
@ApiModel(value="费用申请表")
public class ExpenseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "审核状态（0待提交,1审核中,2审核通过,3驳回）")
    private Integer approvalStatus;

    @ApiModelProperty(value = "申请金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime approvalTime;

    @ApiModelProperty(value = "审核人id")
    private Long approvalId;

    @ApiModelProperty(value = "审核人名称")
    private String approvalName;

    @ApiModelProperty(value = "提交时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "提交人id")
    private Long creatorId;

    @ApiModelProperty(value = "提交人名称")
    private String creatorName;


}
