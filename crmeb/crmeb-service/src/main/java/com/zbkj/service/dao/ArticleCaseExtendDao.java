package com.zbkj.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbkj.common.model.article.ArticleCaseExtend;
import org.apache.ibatis.annotations.Param;

/**
 * 案例文章扩展表 Mapper 接口
 */
public interface ArticleCaseExtendDao extends BaseMapper<ArticleCaseExtend> {

    /**
     * 根据文章ID查询案例扩展信息
     * @param articleId 文章ID
     * @return 案例扩展信息
     */
    default ArticleCaseExtend findByArticleId(@Param("articleId") Long articleId) {
        return selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ArticleCaseExtend>()
                .eq(ArticleCaseExtend::getArticleId, articleId)
                .last("LIMIT 1"));
    }
}
