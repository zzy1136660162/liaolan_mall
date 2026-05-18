package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 留言列表搜索请求
 */
@Data
@ApiModel(value = "ContactMessageSearchRequest对象", description = "留言列表搜索请求")
public class ContactMessageSearchRequest implements Serializable {

    @ApiModelProperty(value = "联系人姓名")
    @Size(max = 32, message = "联系人姓名长度不能超过32个字符")
    private String name;

    @ApiModelProperty(value = "手机号")
    @Size(max = 20, message = "手机号长度不能超过20个字符")
    private String mobile;

    @ApiModelProperty(value = "来源页面")
    @Size(max = 64, message = "来源页面长度不能超过64个字符")
    private String sourcePage;

    @ApiModelProperty(value = "处理状态：0待处理 1已联系 2无效线索 3转商机")
    @Min(value = 0, message = "处理状态不正确")
    @Max(value = 3, message = "处理状态不正确")
    private Integer status;
}
