package com.zbkj.common.model.article;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 案例文章扩展表
 */
@Data
@TableName("eb_article_case_extend")
public class ArticleCaseExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 文章主键ID */
    private Long articleId;

    /** 项目名称 */
    private String projectName;

    /** 行业分类 */
    private String industryCategory;

    /** 区域分类 */
    private String regionCategory;

    /** 项目类型 */
    private String projectType;

    /** 项目地点 */
    private String projectAddress;

    /** 项目周期 */
    private String projectPeriod;

    /** 项目背景 */
    private String projectBackground;

    /** 供货产品说明 */
    private String supplyProducts;

    /** 实施效果 */
    private String implementationResult;

    /** 关联商品ID集合(JSON) */
    private String productIds;

    /** 案例图片集合(JSON) */
    private String coverImages;

    /** 排序值 */
    private Integer sort;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
