package com.zbkj.common.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 关于页内容块 - 修改请求体

 * 后台管理员编辑板块时提交的数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AboutPageBlockRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内容块ID", required = true)
    @NotNull(message = "ID不能为空")
    @Min(value = 1, message = "ID不合法")
    private Integer id;

    @ApiModelProperty(value = "主标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "内容（富文本或JSON）")
    private String content;

    @ApiModelProperty(value = "图片URL集合（JSON数组）")
    private String imageUrls;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "1=启用 0=停用")
    private Boolean status;
}
