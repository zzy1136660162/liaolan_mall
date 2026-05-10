package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 案例文章扩展 Request
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ArticleCaseExtendRequest对象", description = "案例文章扩展表")
public class ArticleCaseExtendRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "行业分类")
    private String industryCategory;

    @ApiModelProperty(value = "区域分类")
    private String regionCategory;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "项目地点")
    private String projectAddress;

    @ApiModelProperty(value = "项目周期")
    private String projectPeriod;

    @ApiModelProperty(value = "项目背景")
    private String projectBackground;

    @ApiModelProperty(value = "供货产品")
    private String supplyProducts;

    @ApiModelProperty(value = "实施效果")
    private String implementationResult;

    @ApiModelProperty(value = "关联商品ID集合(JSON数组)")
    private String productIds;

    @ApiModelProperty(value = "案例图片集合(JSON数组)")
    private String coverImages;

    @ApiModelProperty(value = "排序值")
    private Integer sort;
}
