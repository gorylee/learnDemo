package com.example.quartz.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("ids/id")
public class Ids implements Serializable {

    @ApiModelProperty(value = "ids")
    private Long[] ids;
    @ApiModelProperty(value = "id")
    private Long id;
}
