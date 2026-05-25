package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 留言处理请求
 */
@Data
@ApiModel(value = "ContactMessageHandleRequest对象", description = "留言处理请求")
public class ContactMessageHandleRequest implements Serializable {

    @ApiModelProperty(value = "处理状态：0待处理 1已联系 2无效线索 3转商机", required = true)
    @NotNull(message = "处理状态不能为空")
    @Min(value = 0, message = "处理状态不正确")
    @Max(value = 3, message = "处理状态不正确")
    private Integer status;

    @ApiModelProperty(value = "处理备注")
    @Size(max = 255, message = "处理备注长度不能超过255个字符")
    private String handleRemark;
}
