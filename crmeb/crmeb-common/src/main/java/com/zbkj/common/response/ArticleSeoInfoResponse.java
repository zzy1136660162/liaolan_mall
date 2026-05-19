package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ArticleSeoInfoResponse", description = "Article SEO and share response")
public class ArticleSeoInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

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
}
