package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 案例列表响应
 */
@Data
@ApiModel(value = "CaseListResponse对象", description = "案例列表响应")
public class CaseListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "案例ID")
    private Integer id;

    @ApiModelProperty(value = "案例标题")
    private String title;

    @ApiModelProperty(value = "封面图")
    private String imageInput;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "行业分类")
    private String industryCategory;

    @ApiModelProperty(value = "区域分类")
    private String regionCategory;

    @ApiModelProperty(value = "项目类型")
    private String projectType;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "浏览量")
    private Integer visit;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "更新时间")
    private String updateTime;
}
