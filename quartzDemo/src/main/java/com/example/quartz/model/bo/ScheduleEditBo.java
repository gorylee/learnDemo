package com.example.quartz.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
* @author GorryLee
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Schedule修改对象", description="定时任务")
public class ScheduleEditBo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id",required = true)
    @NotNull
    private Long id;


}
