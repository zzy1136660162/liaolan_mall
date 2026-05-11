package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 案例搜索请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CaseSearchRequest对象", description = "案例搜索请求")
public class CaseSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "搜索关键字")
    private String keywords;

    @ApiModelProperty(value = "行业分类")
    private String industryCategory;

    @ApiModelProperty(value = "区域分类")
    private String regionCategory;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "状态 0关闭 1开启")
    private Boolean status;
}
