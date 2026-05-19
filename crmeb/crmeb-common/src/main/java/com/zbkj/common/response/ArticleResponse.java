package com.zbkj.common.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value = "ArticleResponse", description = "Article response")
public class ArticleResponse implements Serializable {

    private static final long serialVersionUID = -4585094537501770138L;

    @ApiModelProperty(value = "Article id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "Title")
    private String title;

    @ApiModelProperty(value = "Category id")
    private String cid;

    @ApiModelProperty(value = "Author")
    private String author;

    @ApiModelProperty(value = "Cover image")
    private String imageInput;

    @ApiModelProperty(value = "Synopsis")
    private String synopsis;

    @ApiModelProperty(value = "Share title")
    private String shareTitle;

    @ApiModelProperty(value = "Share synopsis")
    private String shareSynopsis;

    @ApiModelProperty(value = "Visits")
    private String visit;

    @ApiModelProperty(value = "Sort")
    private Integer sort;

    @ApiModelProperty(value = "Original url")
    private String url;

    @ApiModelProperty(value = "Bound product id")
    private Integer productId;

    @ApiModelProperty(value = "Hot flag")
    private Boolean isHot;

    @ApiModelProperty(value = "Banner flag")
    private Boolean isBanner;

    @ApiModelProperty(value = "Content")
    private String content;

    @ApiModelProperty(value = "SEO and share data")
    private ArticleSeoInfoResponse seoInfo;

    @ApiModelProperty(value = "Created time")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}
