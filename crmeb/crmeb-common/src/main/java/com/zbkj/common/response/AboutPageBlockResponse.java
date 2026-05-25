package com.zbkj.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 关于页内容块 - 响应对象

 * 返回给前台/后台的展示数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AboutPageBlockResponse对象", description = "关于页内容块响应对象")
public class AboutPageBlockResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "板块标识")
    private String blockKey;

    @ApiModelProperty(value = "板块名称")
    private String blockName;

    @ApiModelProperty(value = "主标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "图片URL集合（JSON数组）")
    private String imageUrls;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "是否启用")
    private Boolean status;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
