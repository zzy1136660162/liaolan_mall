package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 案例请求参数（扁平化，合并文章+扩展字段）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CaseRequest对象", description = "案例请求参数")
public class CaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "案例标题")
    private String title;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "封面图")
    private String imageInput;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "分享标题")
    private String shareTitle;

    @ApiModelProperty(value = "分享简介")
    private String shareSynopsis;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "原文链接")
    private String url;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "是否隐藏")
    private Boolean hide;

    @ApiModelProperty(value = "是否热门")
    private Boolean isHot;

    @ApiModelProperty(value = "是否轮播图")
    private Boolean isBanner;

    @ApiModelProperty(value = "文章内容")
    private String content;

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

    @ApiModelProperty(value = "供货产品说明")
    private String supplyProducts;

    @ApiModelProperty(value = "实施效果")
    private String implementationResult;

    @ApiModelProperty(value = "关联商品ID集合(JSON数组)")
    private String productIds;

    @ApiModelProperty(value = "案例图片集合(JSON数组)")
    private String coverImages;
}
