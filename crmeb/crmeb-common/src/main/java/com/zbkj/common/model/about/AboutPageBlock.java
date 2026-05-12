package com.zbkj.common.model.about;

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

/**
 * 关于页内容块表 Model

 * 一张表存6个固定板块的内容（公司介绍、发展历程、资质荣誉、生产实力、企业文化、联系方式）
 * 每个板块通过 block_key 区分，内容格式因板块而异
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("eb_about_page_block")
@ApiModel(value = "AboutPageBlock对象", description = "关于页内容块表")
public class AboutPageBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "板块标识（唯一）")
    private String blockKey;

    @ApiModelProperty(value = "板块名称")
    private String blockName;

    @ApiModelProperty(value = "主标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "内容（富文本或JSON）")
    private String content;

    @ApiModelProperty(value = "图片URL集合（JSON数组）")
    private String imageUrls;

    @ApiModelProperty(value = "排序值（越小越靠前）")
    private Integer sort;

    @ApiModelProperty(value = "1=启用 0=停用")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
