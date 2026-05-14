package com.zbkj.common.model.download;

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
@TableName("eb_download_material_record")
@ApiModel(value = "DownloadMaterialRecord对象", description = "资料下载记录表")
public class DownloadMaterialRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "资料ID")
    private Integer materialId;

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
