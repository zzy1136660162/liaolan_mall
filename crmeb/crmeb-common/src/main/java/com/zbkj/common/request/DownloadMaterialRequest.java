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
@ApiModel(value = "DownloadMaterialRequest对象", description = "资料下载保存请求")
public class DownloadMaterialRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "资料标题", required = true)
    @NotBlank(message = "请填写资料标题")
    @Length(max = 255, message = "资料标题最多255个字符")
    private String title;

    @ApiModelProperty(value = "分类ID", required = true)
    @NotNull(message = "请选择资料分类")
    private Integer categoryId;

    @ApiModelProperty(value = "资料简介")
    @Length(max = 500, message = "资料简介最多500个字符")
    private String description;

    @ApiModelProperty(value = "文件地址", required = true)
    @NotBlank(message = "请上传资料文件")
    @Length(max = 500, message = "文件地址最多500个字符")
    private String fileUrl;

    @ApiModelProperty(value = "文件类型")
    @Length(max = 32, message = "文件类型最多32个字符")
    private String fileType;

    @ApiModelProperty(value = "封面图")
    @Length(max = 255, message = "封面图地址最多255个字符")
    private String coverImage;

    @ApiModelProperty(value = "是否公开展示", required = true)
    @NotNull(message = "请选择是否公开展示")
    private Boolean isPublic;

    @ApiModelProperty(value = "是否需要留资", required = true)
    @NotNull(message = "请选择是否需要留资")
    private Boolean needForm;

    @ApiModelProperty(value = "状态", required = true)
    @NotNull(message = "请选择资料状态")
    private Boolean status;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
