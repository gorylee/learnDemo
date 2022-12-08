package com.example.quartz.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="Schedule对象", description="定时任务")
public class ScheduleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类名")
    private String className;

    @ApiModelProperty(value = "方法名")
    private String methodName;

    @ApiModelProperty(value = "cron表达式")
    private String cron;

    @ApiModelProperty(value = "状态，1启用，-1禁用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人id")
    private Long creatorId;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后修改人id")
    private Long modifierId;

    @ApiModelProperty(value = "最后修改人名称")
    private String modifierName;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "已删除(0：否，1：是)")
    private Integer deleted;


}
