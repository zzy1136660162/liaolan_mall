package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 联系方式信息响应
 */
@Data
@ApiModel(value = "ContactInfoResponse对象", description = "联系方式信息响应")
public class ContactInfoResponse implements Serializable {

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系电话")
    private String consumerHotline;

    @ApiModelProperty(value = "公司地址")
    private String companyAddress;

    @ApiModelProperty(value = "地图跳转链接")
    private String mapLink;

    @ApiModelProperty(value = "在线客服链接")
    private String serviceLink;

    @ApiModelProperty(value = "客服二维码")
    private String serviceQrcode;
}
