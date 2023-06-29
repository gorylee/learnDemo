package com.example.camunda.module.bo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "分页查询")
public class BaseQueryBo {
    @ApiModelProperty(value = "页码",required = true)
    private Long pageNo=1l;
    @ApiModelProperty(value = "页面大小",required = true)
    private Long pageSize=10l;
    @ApiModelProperty(value = "排序字段")
    private String sortField;
    @ApiModelProperty(value = "排序规则(1: 升序 2: 降序)")
    private Integer sortRule;
    @ApiModelProperty(value = "删除状态")
    private Integer deleted = 0;
    @ApiModelProperty(value = "是否上行锁")
    private boolean isForUpdate;
    @ApiModelProperty(value = "是否查缓存")
    private Boolean isCache;
    @ApiModelProperty(value = "缓存时间second")
    private Long expireTime;

    public String getSortField() {
        return StrUtil.toUnderlineCase(sortField);
    }
}
