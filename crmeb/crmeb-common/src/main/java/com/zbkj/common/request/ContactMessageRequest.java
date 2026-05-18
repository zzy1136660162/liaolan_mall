package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 前台留言提交请求
 */
@Data
@ApiModel(value = "ContactMessageRequest对象", description = "前台留言提交请求")
public class ContactMessageRequest implements Serializable {

    @ApiModelProperty(value = "公司名称")
    @Size(max = 100, message = "公司名称长度不能超过100个字符")
    private String companyName;

    @ApiModelProperty(value = "联系人姓名", required = true)
    @NotBlank(message = "联系人姓名不能为空")
    @Size(max = 32, message = "联系人姓名长度不能超过32个字符")
    private String name;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1(3|4|5|6|7|8|9)\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    @ApiModelProperty(value = "留言内容", required = true)
    @NotBlank(message = "留言内容不能为空")
    @Size(max = 500, message = "留言内容长度不能超过500个字符")
    private String content;

    @ApiModelProperty(value = "来源页面")
    @Size(max = 64, message = "来源页面长度不能超过64个字符")
    private String sourcePage;
}
