package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ArticleSeoInfoRequest", description = "Article SEO and share data")
public class ArticleSeoInfoRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "SEO title")
    @Length(max = 255, message = "SEO title must be at most 255 characters")
    private String seoTitle;

    @ApiModelProperty(value = "SEO keywords")
    @Length(max = 255, message = "SEO keywords must be at most 255 characters")
    private String seoKeywords;

    @ApiModelProperty(value = "SEO description")
    @Length(max = 500, message = "SEO description must be at most 500 characters")
    private String seoDescription;

    @ApiModelProperty(value = "Share title")
    @Length(max = 255, message = "Share title must be at most 255 characters")
    private String shareTitle;

    @ApiModelProperty(value = "Share image")
    @Length(max = 255, message = "Share image must be at most 255 characters")
    private String shareImage;
}
