package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "OrderDeliveryExtendRequest对象", description = "订单履约扩展信息请求")
public class OrderDeliveryExtendRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号")
    private String deliveryNo;

    @ApiModelProperty(value = "发货备注")
    private String deliveryRemark;

    @ApiModelProperty(value = "线缆批次号")
    private String batchNo;

    @ApiModelProperty(value = "出厂编号")
    private String factorySerialNo;
}
