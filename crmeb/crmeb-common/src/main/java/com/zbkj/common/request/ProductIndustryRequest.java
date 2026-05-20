package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ProductIndustryRequest对象", description = "商品行业扩展信息请求")
public class ProductIndustryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "证书文件集合")
    private List<String> certificateFiles;

    @ApiModelProperty(value = "关联资料ID集合")
    private List<Integer> downloadFileIds;
}
