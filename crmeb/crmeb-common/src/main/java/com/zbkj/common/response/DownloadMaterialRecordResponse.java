package com.zbkj.common.response;

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
@ApiModel(value = "DownloadMaterialRecordResponse对象", description = "资料下载记录返回对象")
public class DownloadMaterialRecordResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录ID")
    private Integer id;

    @ApiModelProperty(value = "资料ID")
    private Integer materialId;

    @ApiModelProperty(value = "资料标题")
    private String materialTitle;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "联系人")
    private String contactName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "需求说明")
    private String requirementDesc;

    @ApiModelProperty(value = "来源页")
    private String sourcePage;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
