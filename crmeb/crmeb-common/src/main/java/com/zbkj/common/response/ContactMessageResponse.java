package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 联系我们留言响应
 */
@Data
@ApiModel(value = "ContactMessageResponse对象", description = "联系我们留言响应")
public class ContactMessageResponse implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系人姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "留言内容")
    private String content;

    @ApiModelProperty(value = "来源页面")
    private String sourcePage;

    @ApiModelProperty(value = "处理状态：0待处理 1已联系 2无效线索 3转商机")
    private Integer status;

    @ApiModelProperty(value = "处理状态文本")
    private String statusText;

    @ApiModelProperty(value = "处理备注")
    private String handleRemark;

    @ApiModelProperty(value = "处理人ID")
    private Integer handleUserId;

    @ApiModelProperty(value = "处理人姓名")
    private String handleUserName;

    @ApiModelProperty(value = "处理时间")
    private Date handleTime;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;
}
