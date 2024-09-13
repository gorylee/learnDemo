package com.example.quartz.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
* @author GoryLee
* @date  2023-12-20
*/
@Data
@Accessors(chain = true)
@ApiModel(value="客户-附加服务关联表")
public class CustomerAdditionalServicesVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司附加服务ID")
    private Long companyAdditionalId;

    @ApiModelProperty(value = "附加服务IDs")
    private String additionalServicesIds;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "订单附加服务名称")
    private String saleAdditionalServicesName;

    @ApiModelProperty(value = "商品附加服务名称")
    private String goodsAdditionalServicesName;

    @ApiModelProperty(value = "修改时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
//    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyTime;

}
