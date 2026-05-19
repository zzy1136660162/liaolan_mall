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
@ApiModel(value = "DownloadMaterialRecordSearchRequest对象", description = "资料下载记录查询请求")
public class DownloadMaterialRecordSearchRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资料ID")
    private Integer materialId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "来源页")
    private String sourcePage;

    @ApiModelProperty(value = "开始时间")
    private String startDate;

    @ApiModelProperty(value = "结束时间")
    private String endDate;
}
