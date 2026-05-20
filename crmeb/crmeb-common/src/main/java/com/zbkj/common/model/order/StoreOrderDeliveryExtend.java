package com.zbkj.common.model.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_store_order_delivery_extend")
@ApiModel(value = "StoreOrderDeliveryExtend对象", description = "订单履约扩展表-发货补充字段")
public class StoreOrderDeliveryExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单主键ID")
    private Integer orderId;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
