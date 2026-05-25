package com.zbkj.common.model.article;

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
@TableName("eb_article_seo_extend")
@ApiModel(value = "ArticleSeoExtend", description = "Article SEO extension")
public class ArticleSeoExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "Primary key")
    private Integer id;

    @ApiModelProperty(value = "Article id")
    private Integer articleId;

    @ApiModelProperty(value = "SEO title")
    private String seoTitle;

    @ApiModelProperty(value = "SEO keywords")
    private String seoKeywords;

    @ApiModelProperty(value = "SEO description")
    private String seoDescription;

    @ApiModelProperty(value = "Share title")
    private String shareTitle;

    @ApiModelProperty(value = "Share image")
    private String shareImage;

    @ApiModelProperty(value = "Created time")
    private Date createTime;

    @ApiModelProperty(value = "Updated time")
    private Date updateTime;
}
