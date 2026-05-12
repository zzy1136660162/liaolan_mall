package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "DownloadMaterialRecordRequest对象", description = "资料下载留资请求")
public class DownloadMaterialRecordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资料ID", required = true)
    @NotNull(message = "资料ID不能为空")
    private Integer materialId;

    @ApiModelProperty(value = "公司名称")
    @Length(max = 255, message = "公司名称最多255个字符")
    private String companyName;

    @ApiModelProperty(value = "联系人")
    @Length(max = 128, message = "联系人最多128个字符")
    private String contactName;

    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "请填写手机号")
    @Length(max = 32, message = "手机号最多32个字符")
    private String phone;

    @ApiModelProperty(value = "需求说明")
    @Length(max = 500, message = "需求说明最多500个字符")
    private String requirementDesc;

    @ApiModelProperty(value = "来源页")
    @Length(max = 64, message = "来源页最多64个字符")
    private String sourcePage;
}
