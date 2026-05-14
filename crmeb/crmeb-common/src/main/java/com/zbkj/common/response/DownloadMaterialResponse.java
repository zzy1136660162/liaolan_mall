package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DownloadMaterialResponse对象", description = "资料下载返回对象")
public class DownloadMaterialResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资料ID")
    private Integer id;

    @ApiModelProperty(value = "资料标题")
    private String title;

    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;

    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    @ApiModelProperty(value = "资料简介")
    private String description;

    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "封面图")
    private String coverImage;

    @ApiModelProperty(value = "是否公开展示")
    private Boolean isPublic;

    @ApiModelProperty(value = "是否需要留资")
    private Boolean needForm;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "下载次数")
    private Integer downloadCount;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
