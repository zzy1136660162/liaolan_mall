package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.article.Article;
import com.zbkj.common.model.article.ArticleSeoExtend;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ArticleRequest;
import com.zbkj.common.request.ArticleSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ArticleSeoInfoResponse;
import com.zbkj.common.response.ArticleResponse;
import com.zbkj.common.vo.ArticleVo;
import com.zbkj.service.dao.ArticleDao;
import com.zbkj.service.service.ArticleSeoExtendService;
import com.zbkj.service.service.ArticleService;
import com.zbkj.service.service.CategoryService;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ArticleServiceImpl 鎺ュ彛瀹炵幇
 *  +----------------------------------------------------------------------
 *  | CRMEB [ CRMEB璧嬭兘寮€鍙戣€咃紝鍔╁姏浼佷笟鍙戝睍 ]
 *  +----------------------------------------------------------------------
 *  | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
 *  +----------------------------------------------------------------------
 *  | Licensed CRMEB骞朵笉鏄嚜鐢辫蒋浠讹紝鏈粡璁稿彲涓嶈兘鍘绘帀CRMEB鐩稿叧鐗堟潈
 *  +----------------------------------------------------------------------
 *  | Author: CRMEB Team <admin@crmeb.com>
 *  +----------------------------------------------------------------------
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    private final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Resource
    private ArticleDao dao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private ArticleSeoExtendService articleSeoExtendService;

    /**
     * 鍒楄〃
     * @param cid 鏂囩珷鍒嗙被id
     * @param pageParamRequest 鍒嗛〉绫诲弬鏁?
     * @return PageInfo<ArticleResponse>
     */
    @Override
    public PageInfo<ArticleResponse> getList(String cid, PageParamRequest pageParamRequest) {
        Page<Article> articlePage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(Article::getCid, cid);
        lambdaQueryWrapper.eq(Article::getHide, false);
        lambdaQueryWrapper.eq(Article::getStatus, false);
        lambdaQueryWrapper.orderByDesc(Article::getSort)
                .orderByDesc(Article::getVisit)
                .orderByDesc(Article::getCreateTime);
        List<Article> articleList = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(articleList)) {
            return CommonPage.copyPageInfo(articlePage, CollUtil.newArrayList());
        }
        List<ArticleResponse> responseList = articleList.stream().map(article -> {
            ArticleResponse articleResponse = new ArticleResponse();
            BeanUtils.copyProperties(article, articleResponse);
            return articleResponse;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(articlePage, responseList);
    }

    /**
     * 鑾峰彇鏂囩珷鍒楄〃
     * @param request 璇锋眰鍙傛暟
     * @param pageParamRequest 鍒嗛〉鍙傛暟
     * @return PageInfo
     */
    @Override
    public PageInfo<ArticleVo> getAdminList(ArticleSearchRequest request, PageParamRequest pageParamRequest) {
        Page<Article> articlePage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(request.getCid())) {
            lambdaQueryWrapper.eq(Article::getCid, request.getCid());
        }
        if (!StringUtils.isBlank(request.getKeywords())) {
            lambdaQueryWrapper.and(i -> i.or().like(Article::getTitle, request.getKeywords())
                    .or().like(Article::getAuthor, request.getKeywords())
                    .or().like(Article::getSynopsis, request.getKeywords()));
        }
        lambdaQueryWrapper.orderByDesc(Article::getVisit).orderByDesc(Article::getId);
        List<Article> articleList = dao.selectList(lambdaQueryWrapper);

        ArrayList<ArticleVo> articleVoArrayList = new ArrayList<>();
        if (articleList.isEmpty()) {
            return CommonPage.copyPageInfo(articlePage, articleVoArrayList);
        }
        for (Article article : articleList) {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(article, articleVo);
            if (!StrUtil.isBlank(article.getImageInput())) {
                articleVo.setImageInput(article.getImageInput());
            }
            articleVoArrayList.add(articleVo);
        }
        return CommonPage.copyPageInfo(articlePage, articleVoArrayList);
    }

    /**
     * 鏌ヨ鏂囩珷璇︽儏
     * @param id Integer
     * @return ArticleResponse
     */
    @Override
    public ArticleResponse getVoByFront(Integer id) {
        Article article = getById(id);
        if (ObjectUtil.isNull(article) || article.getStatus()) {
            throw new CrmebException("鏂囩珷涓嶅瓨鍦?");
        }

        ArticleResponse articleResponse = buildDetailResponse(article);

        String visit = StrUtil.isNotBlank(article.getVisit()) ? article.getVisit() : "0";
        int num = Integer.parseInt(visit) + 1;
        article.setVisit(String.valueOf(num));
        article.setUpdateTime(DateUtil.date());
        dao.updateById(article);
        return articleResponse;
    }

    /**
     * 鑾峰彇绉诲姩绔痓anner鍒楄〃
     * @return List<Article>
     */
    @Override
    public List<Article> getBannerList() {
        String articleBannerLimitString = systemConfigService.getValueByKey(Constants.ARTICLE_BANNER_LIMIT);
        int articleBannerLimit = Integer.parseInt(articleBannerLimitString);

        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(Article::getId, Article::getImageInput);
        lambdaQueryWrapper.eq(Article::getIsBanner, true);
        lambdaQueryWrapper.eq(Article::getHide, false);
        lambdaQueryWrapper.eq(Article::getStatus, false);
        lambdaQueryWrapper.orderByDesc(Article::getSort);
        lambdaQueryWrapper.last(" limit " + articleBannerLimit);
        return dao.selectList(lambdaQueryWrapper);
    }

    /**
     * 鑾峰彇绉诲姩绔儹闂ㄥ垪琛?
     * @return List<ArticleResponse>
     */
    @Override
    public List<ArticleResponse> getHotList() {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(Article::getId, Article::getImageInput, Article::getTitle, Article::getCreateTime);
        lambdaQueryWrapper.eq(Article::getIsHot, true);
        lambdaQueryWrapper.eq(Article::getHide, false);
        lambdaQueryWrapper.eq(Article::getStatus, false);
        lambdaQueryWrapper.orderByDesc(Article::getSort);
        lambdaQueryWrapper.last(" limit 20");
        List<Article> articleList = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(articleList)) {
            return CollUtil.newArrayList();
        }
        return articleList.stream().map(article -> {
            ArticleResponse articleResponse = new ArticleResponse();
            BeanUtils.copyProperties(article, articleResponse);
            return articleResponse;
        }).collect(Collectors.toList());
    }

    /**
     * 鑾峰彇鏂囩珷鍒嗙被鍒楄〃(绉诲姩绔?
     * @return List<Category>
     */
    @Override
    public List<Category> getCategoryList() {
        return categoryService.findArticleCategoryList();
    }

    /**
     * 鏂囩珷鏂板
     * @param articleRequest 鏂囩珷鏂板鍙傛暟
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ArticleRequest articleRequest) {
        Article article = new Article();
        BeanUtils.copyProperties(articleRequest, article);
        article.setImageInput(systemAttachmentService.clearPrefix(article.getImageInput()));
        article.setContent(systemAttachmentService.clearPrefix(article.getContent()));
        article.setShareTitle(resolveShareTitle(articleRequest));
        article.setShareSynopsis(resolveShareDescription(articleRequest));
        article.setVisit("0");
        boolean saved = save(article);
        if (!saved) {
            return false;
        }
        return articleSeoExtendService.saveOrUpdateByArticleId(article.getId(), articleRequest.getSeoInfo());
    }

    /**
     * 鏂囩珷鍒犻櫎
     * @param id 鏂囩珷id
     * @return Boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Integer id) {
        Article article = getById(id);
        if (ObjectUtil.isNull(article)) {
            throw new CrmebException("鏂囩珷宸插垹闄?");
        }
        boolean removed = removeById(id);
        if (!removed) {
            return false;
        }
        return articleSeoExtendService.deleteByArticleId(id);
    }

    /**
     * 鏂囩珷淇敼
     * @param id 鏂囩珷id
     * @param articleRequest 鏂囩珷淇敼鍙傛暟
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateArticle(Integer id, ArticleRequest articleRequest) {
        Article article = new Article();
        BeanUtils.copyProperties(articleRequest, article);
        article.setId(id);
        article.setImageInput(systemAttachmentService.clearPrefix(article.getImageInput()));
        article.setContent(systemAttachmentService.clearPrefix(article.getContent()));
        article.setShareTitle(resolveShareTitle(articleRequest));
        article.setShareSynopsis(resolveShareDescription(articleRequest));
        article.setUpdateTime(DateUtil.date());
        boolean updated = updateById(article);
        if (!updated) {
            return false;
        }
        return articleSeoExtendService.saveOrUpdateByArticleId(id, articleRequest.getSeoInfo());
    }

    /**
     * 鑾峰彇鏂囩珷璇︽儏
     * @param id 鏂囩珷id
     * @return ArticleResponse
     */
    @Override
    public ArticleResponse getDetail(Integer id) {
        Article article = getById(id);
        if (ObjectUtil.isNull(article)) {
            throw new CrmebException("鏂囩珷涓嶅瓨鍦?");
        }
        return buildDetailResponse(article);
    }

    private ArticleResponse buildDetailResponse(Article article) {
        ArticleResponse articleResponse = new ArticleResponse();
        BeanUtils.copyProperties(article, articleResponse);
        ArticleSeoInfoResponse seoInfo = buildSeoInfo(article, articleSeoExtendService.getByArticleId(article.getId()));
        articleResponse.setSeoInfo(seoInfo);
        articleResponse.setShareTitle(seoInfo.getShareTitle());
        articleResponse.setShareSynopsis(seoInfo.getSeoDescription());
        return articleResponse;
    }

    private ArticleSeoInfoResponse buildSeoInfo(Article article, ArticleSeoExtend articleSeoExtend) {
        ArticleSeoInfoResponse seoInfo = new ArticleSeoInfoResponse();
        seoInfo.setSeoTitle(firstNonBlank(articleSeoExtend == null ? null : articleSeoExtend.getSeoTitle(), article.getTitle()));
        seoInfo.setSeoKeywords(firstNonBlank(articleSeoExtend == null ? null : articleSeoExtend.getSeoKeywords(), ""));
        seoInfo.setSeoDescription(firstNonBlank(articleSeoExtend == null ? null : articleSeoExtend.getSeoDescription(),
                article.getShareSynopsis(), article.getSynopsis()));
        seoInfo.setShareTitle(firstNonBlank(articleSeoExtend == null ? null : articleSeoExtend.getShareTitle(),
                article.getShareTitle(), article.getTitle()));
        seoInfo.setShareImage(firstNonBlank(articleSeoExtend == null ? null : articleSeoExtend.getShareImage(),
                article.getImageInput()));
        return seoInfo;
    }

    private String resolveShareTitle(ArticleRequest articleRequest) {
        if (articleRequest.getSeoInfo() != null && StrUtil.isNotBlank(articleRequest.getSeoInfo().getShareTitle())) {
            return articleRequest.getSeoInfo().getShareTitle();
        }
        return firstNonBlank(articleRequest.getShareTitle(), articleRequest.getTitle());
    }

    private String resolveShareDescription(ArticleRequest articleRequest) {
        if (articleRequest.getSeoInfo() != null && StrUtil.isNotBlank(articleRequest.getSeoInfo().getSeoDescription())) {
            return articleRequest.getSeoInfo().getSeoDescription();
        }
        return firstNonBlank(articleRequest.getShareSynopsis(), articleRequest.getSynopsis());
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StrUtil.isNotBlank(value)) {
                return value;
            }
        }
        return "";
    }
}
