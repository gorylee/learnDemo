package com.example.quartz.model.bo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
* @author GoryLee
* @date:  2023-12-20
*/
@Data
@Accessors(chain = true)
@ApiModel(value="客户-附加服务关联表查询CustomerAdditionalServicesQueryBo")
public class CustomerAdditionalServicesQueryBo {


    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司附加服务ID")
    private Long companyAdditionalId;

    @ApiModelProperty(value = "附加服务IDs集合")
    private List<Long> additionalServicesIds;

    @ApiModelProperty(value = "调出公司ID")
    private Long transferOutCompanyId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;


    @ApiModelProperty(value = "客户id")
    private Long customerId;



}
