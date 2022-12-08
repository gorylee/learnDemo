package com.example.quartz.model.bo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author GorryLee
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Schedule新增对象", description="定时任务")
public class ScheduleAddBo implements Serializable {

    private static final long serialVersionUID = 1L;


}
