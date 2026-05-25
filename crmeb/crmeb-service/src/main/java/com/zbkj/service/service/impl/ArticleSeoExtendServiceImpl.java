package com.zbkj.service.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.article.ArticleSeoExtend;
import com.zbkj.common.request.ArticleSeoInfoRequest;
import com.zbkj.service.dao.ArticleSeoExtendDao;
import com.zbkj.service.service.ArticleSeoExtendService;
import com.zbkj.service.service.SystemAttachmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 鏂囩珷 SEO 鎵╁睍鏈嶅姟瀹炵幇
 */
@Service
public class ArticleSeoExtendServiceImpl extends ServiceImpl<ArticleSeoExtendDao, ArticleSeoExtend>
        implements ArticleSeoExtendService {

    @Resource
    private ArticleSeoExtendDao dao;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Override
    public ArticleSeoExtend getByArticleId(Integer articleId) {
        LambdaQueryWrapper<ArticleSeoExtend> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ArticleSeoExtend::getArticleId, articleId);
        lambdaQueryWrapper.last(" limit 1");
        return dao.selectOne(lambdaQueryWrapper);
    }

    @Override
    public Boolean saveOrUpdateByArticleId(Integer articleId, ArticleSeoInfoRequest seoInfoRequest) {
        if (isEmpty(seoInfoRequest)) {
            return deleteByArticleId(articleId);
        }

        ArticleSeoExtend existing = getByArticleId(articleId);
        ArticleSeoExtend articleSeoExtend = new ArticleSeoExtend();
        BeanUtils.copyProperties(seoInfoRequest, articleSeoExtend);
        articleSeoExtend.setArticleId(articleId);
        articleSeoExtend.setShareImage(systemAttachmentService.clearPrefix(articleSeoExtend.getShareImage()));
        articleSeoExtend.setUpdateTime(DateUtil.date());
        if (existing == null) {
            articleSeoExtend.setCreateTime(DateUtil.date());
            return save(articleSeoExtend);
        }
        articleSeoExtend.setId(existing.getId());
        articleSeoExtend.setCreateTime(existing.getCreateTime());
        return updateById(articleSeoExtend);
    }

    @Override
    public Boolean deleteByArticleId(Integer articleId) {
        LambdaQueryWrapper<ArticleSeoExtend> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(ArticleSeoExtend::getArticleId, articleId);
        if (dao.selectCount(lambdaQueryWrapper) < 1) {
            return true;
        }
        return remove(lambdaQueryWrapper);
    }

    private boolean isEmpty(ArticleSeoInfoRequest seoInfoRequest) {
        return seoInfoRequest == null
                || (StrUtil.isBlank(seoInfoRequest.getSeoTitle())
                && StrUtil.isBlank(seoInfoRequest.getSeoKeywords())
                && StrUtil.isBlank(seoInfoRequest.getSeoDescription())
                && StrUtil.isBlank(seoInfoRequest.getShareTitle())
                && StrUtil.isBlank(seoInfoRequest.getShareImage()));
    }
}
