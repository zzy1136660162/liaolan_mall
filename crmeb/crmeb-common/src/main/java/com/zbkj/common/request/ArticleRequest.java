package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ArticleRequest", description = "Article request")
public class ArticleRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Category id", required = true)
    @NotBlank(message = "Category is required")
    private String cid;

    @ApiModelProperty(value = "Article title", required = true)
    @NotBlank(message = "Title is required")
    @Length(max = 200, message = "Title must be at most 200 characters")
    private String title;

    @ApiModelProperty(value = "Author", required = true)
    @NotBlank(message = "Author is required")
    @Length(max = 50, message = "Author must be at most 50 characters")
    private String author;

    @ApiModelProperty(value = "Cover image", required = true)
    @NotBlank(message = "Cover image is required")
    @Length(max = 255, message = "Cover image must be at most 255 characters")
    private String imageInput;

    @ApiModelProperty(value = "Synopsis", required = true)
    @Length(max = 200, message = "Synopsis must be at most 200 characters")
    @NotBlank(message = "Synopsis is required")
    private String synopsis;

    @ApiModelProperty(value = "Legacy share title")
    @Length(max = 200, message = "Share title must be at most 200 characters")
    private String shareTitle;

    @ApiModelProperty(value = "Legacy share synopsis")
    @Length(max = 200, message = "Share synopsis must be at most 200 characters")
    private String shareSynopsis;

    @ApiModelProperty(value = "SEO and share data")
    @Valid
    private ArticleSeoInfoRequest seoInfo;

    @ApiModelProperty(value = "Is hot", example = "false")
    @NotNull(message = "Hot flag is required")
    private Boolean isHot;

    @ApiModelProperty(value = "Is banner", example = "true")
    @NotNull(message = "Banner flag is required")
    private Boolean isBanner;

    @ApiModelProperty(value = "Article content", required = true)
    @NotBlank(message = "Content is required")
    private String content;
}
