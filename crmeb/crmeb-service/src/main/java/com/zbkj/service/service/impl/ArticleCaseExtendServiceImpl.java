package com.zbkj.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbkj.common.model.article.ArticleCaseExtend;
import com.zbkj.service.dao.ArticleCaseExtendDao;
import com.zbkj.service.service.ArticleCaseExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 案例文章扩展表 服务实现类
 */
@Service
public class ArticleCaseExtendServiceImpl implements ArticleCaseExtendService {

    @Autowired
    private ArticleCaseExtendDao articleCaseExtendDao;

    @Override
    public ArticleCaseExtend getByArticleId(Long articleId) {
        return articleCaseExtendDao.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ArticleCaseExtend>()
                        .eq(ArticleCaseExtend::getArticleId, articleId)
                        .last("LIMIT 1"));
    }

    @Override
    public boolean save(ArticleCaseExtend articleCaseExtend) {
        return articleCaseExtendDao.insert(articleCaseExtend) > 0;
    }

    @Override
    public boolean update(ArticleCaseExtend articleCaseExtend) {
        return articleCaseExtendDao.updateById(articleCaseExtend) > 0;
    }

    @Override
    public boolean deleteByArticleId(Long articleId) {
        ArticleCaseExtend extend = getByArticleId(articleId);
        if (extend != null) {
            return articleCaseExtendDao.deleteById(extend.getId()) > 0;
        }
        return true;
    }

    @Override
    public List<ArticleCaseExtend> listByArticleIds(List<Long> articleIds) {
        LambdaQueryWrapper<ArticleCaseExtend> lqw = Wrappers.lambdaQuery();
        lqw.in(ArticleCaseExtend::getArticleId, articleIds);
        return articleCaseExtendDao.selectList(lqw);
    }

    @Override
    public List<ArticleCaseExtend> listByCondition(String industryCategory, String regionCategory) {
        LambdaQueryWrapper<ArticleCaseExtend> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(industryCategory)) {
            lqw.eq(ArticleCaseExtend::getIndustryCategory, industryCategory);
        }
        if (StrUtil.isNotBlank(regionCategory)) {
            lqw.eq(ArticleCaseExtend::getRegionCategory, regionCategory);
        }
        lqw.select(ArticleCaseExtend::getArticleId);
        return articleCaseExtendDao.selectList(lqw);
    }

    @Override
    public List<String> getDistinctIndustryCategories() {
        LambdaQueryWrapper<ArticleCaseExtend> lqw = Wrappers.lambdaQuery();
        lqw.select(ArticleCaseExtend::getIndustryCategory);
        lqw.groupBy(ArticleCaseExtend::getIndustryCategory);
        lqw.orderByAsc(ArticleCaseExtend::getIndustryCategory);
        return articleCaseExtendDao.selectList(lqw).stream()
                .map(ArticleCaseExtend::getIndustryCategory)
                .collect(Collectors.toList());
    }
}
