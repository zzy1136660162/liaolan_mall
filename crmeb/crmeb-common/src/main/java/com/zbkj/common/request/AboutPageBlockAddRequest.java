package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 关于页内容块 - 新增请求体
 *
 * 后台管理员新增板块时提交的数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "AboutPageBlockAddRequest对象", description = "关于页内容块新增请求")
public class AboutPageBlockAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "板块标识（唯一）", required = true)
    @NotBlank(message = "板块标识不能为空")
    private String blockKey;

    @ApiModelProperty(value = "板块名称", required = true)
    @NotBlank(message = "板块名称不能为空")
    private String blockName;

    @ApiModelProperty(value = "主标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "内容（富文本或JSON）")
    private String content;

    @ApiModelProperty(value = "图片URL集合（JSON数组）")
    private String imageUrls;

    @ApiModelProperty(value = "排序值", required = true)
    @NotNull(message = "排序值不能为空")
    private Integer sort;

    @ApiModelProperty(value = "1=启用 0=停用", required = true)
    @NotNull(message = "状态不能为空")
    private Boolean status;
}
