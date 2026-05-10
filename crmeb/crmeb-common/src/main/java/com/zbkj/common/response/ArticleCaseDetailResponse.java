package com.zbkj.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 案例详情响应（包含关联商品信息）
 */
@Data
@ApiModel(value = "ArticleCaseDetailResponse对象", description = "案例详情响应")
public class ArticleCaseDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    private Integer id;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "封面图")
    private String imageInput;

    @ApiModelProperty(value = "摘要")
    private String synopsis;

    @ApiModelProperty(value = "富文本正文")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "浏览量")
    private Integer visit;

    @ApiModelProperty(value = "案例扩展信息")
    private ArticleCaseExtendResponse caseInfo;

    @ApiModelProperty(value = "关联商品列表")
    private List<ProductSimpleResponse> productList;
}
