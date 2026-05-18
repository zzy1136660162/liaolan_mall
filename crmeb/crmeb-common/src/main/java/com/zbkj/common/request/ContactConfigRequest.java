package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 联系方式配置请求
 */
@Data
@ApiModel(value = "ContactConfigRequest对象", description = "联系方式配置请求")
public class ContactConfigRequest implements Serializable {

    @ApiModelProperty(value = "联系电话")
    @Pattern(regexp = "^$|^(1[3-9]\\d{9}|0\\d{2,3}-?\\d{7,8}|400-?\\d{3}-?\\d{4})$", message = "联系电话格式不正确")
    private String consumerHotline;

    @ApiModelProperty(value = "公司地址")
    @Size(max = 255, message = "公司地址长度不能超过255个字符")
    private String companyAddress;

    @ApiModelProperty(value = "地图跳转链接")
    @Pattern(regexp = "^$|^https?://.*", message = "地图链接格式不正确")
    @Size(max = 500, message = "地图链接长度不能超过500个字符")
    private String mapLink;

    @ApiModelProperty(value = "在线客服链接")
    @Pattern(regexp = "^$|^https?://.*", message = "在线客服链接格式不正确")
    @Size(max = 500, message = "在线客服链接长度不能超过500个字符")
    private String serviceLink;

    @ApiModelProperty(value = "客服二维码")
    @Pattern(regexp = "^$|^(https?://.*|/.*|[A-Za-z0-9_./-]+)$", message = "客服二维码地址格式不正确")
    @Size(max = 500, message = "客服二维码长度不能超过500个字符")
    private String serviceQrcode;
}
