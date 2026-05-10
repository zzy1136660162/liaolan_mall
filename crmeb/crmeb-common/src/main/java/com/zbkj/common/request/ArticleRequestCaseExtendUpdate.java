package com.zbkj.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 文章更新请求参数（包含案例扩展信息）
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "ArticleRequestCaseExtendUpdate对象", description = "文章更新请求参数（包含案例扩展信息）")
public class ArticleRequestCaseExtendUpdate extends ArticleRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "案例扩展信息")
    private ArticleCaseExtendRequest caseInfo;
}
