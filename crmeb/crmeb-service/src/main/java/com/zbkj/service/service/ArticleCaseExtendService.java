package com.zbkj.service.service;

import com.zbkj.common.model.article.ArticleCaseExtend;

import java.util.List;

/**
 * 案例文章扩展表 服务接口
 */
public interface ArticleCaseExtendService {

    /**
     * 根据文章ID获取案例扩展信息
     * @param articleId 文章ID
     * @return 案例扩展信息
     */
    ArticleCaseExtend getByArticleId(Long articleId);

    /**
     * 保存案例扩展信息
     * @param articleCaseExtend 案例扩展信息
     * @return 是否成功
     */
    boolean save(ArticleCaseExtend articleCaseExtend);

    /**
     * 更新案例扩展信息
     * @param articleCaseExtend 案例扩展信息
     * @return 是否成功
     */
    boolean update(ArticleCaseExtend articleCaseExtend);

    /**
     * 根据文章ID删除案例扩展信息
     * @param articleId 文章ID
     * @return 是否成功
     */
    boolean deleteByArticleId(Long articleId);

    /**
     * 根据文章ID集合批量查询案例扩展信息
     * @param articleIds 文章ID集合
     * @return 案例扩展信息列表
     */
    List<ArticleCaseExtend> listByArticleIds(List<Long> articleIds);

    /**
     * 根据行业分类和区域分类查询案例扩展信息
     * @param industryCategory 行业分类（可为空）
     * @param regionCategory 区域分类（可为空）
     * @return 案例扩展信息列表
     */
    List<ArticleCaseExtend> listByCondition(String industryCategory, String regionCategory);

    /**
     * 获取所有去重的行业分类
     * @return 行业分类列表
     */
    List<String> getDistinctIndustryCategories();
}
