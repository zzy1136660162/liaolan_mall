package com.zbkj.common.model.product;

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
@TableName("eb_store_product_industry")
@ApiModel(value = "StoreProductIndustry对象", description = "商品行业扩展表-电线电缆结构化参数")
public class StoreProductIndustry implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "商品主键ID")
    private Integer productId;

    @ApiModelProperty(value = "产品型号")
    private String modelNo;

    @ApiModelProperty(value = "电压等级")
    private String voltageLevel;

    @ApiModelProperty(value = "导体材质")
    private String conductorMaterial;

    @ApiModelProperty(value = "芯数")
    private String coreCount;

    @ApiModelProperty(value = "截面积")
    private String crossSectionArea;

    @ApiModelProperty(value = "护套材质")
    private String sheathMaterial;

    @ApiModelProperty(value = "阻燃等级")
    private String flameRetardantGrade;

    @ApiModelProperty(value = "执行标准")
    private String standardCode;

    @ApiModelProperty(value = "适用场景")
    private String applicationScene;

    @ApiModelProperty(value = "证书文件集合，JSON数组")
    private String certificateFiles;

    @ApiModelProperty(value = "关联资料ID集合，JSON数组")
    private String downloadFileIds;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
