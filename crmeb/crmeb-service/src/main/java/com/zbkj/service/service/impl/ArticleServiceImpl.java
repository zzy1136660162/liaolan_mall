package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.article.Article;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ArticleRequest;
import com.zbkj.common.request.ArticleCaseExtendRequest;
import com.zbkj.common.request.ArticleRequestCaseExtend;
import com.zbkj.common.request.ArticleRequestCaseExtendUpdate;
import com.zbkj.common.request.ArticleSearchRequest;
import com.zbkj.common.request.ArticleFrontSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ArticleCaseDetailResponse;
import com.zbkj.common.response.ArticleCaseExtendResponse;
import com.zbkj.common.response.ArticleCaseListResponse;
import com.zbkj.common.response.ArticleResponse;
import com.zbkj.common.response.ProductSimpleResponse;
import com.zbkj.common.model.article.ArticleCaseExtend;
import com.zbkj.common.vo.ArticleVo;
import com.zbkj.service.service.ArticleCaseExtendService;
import com.zbkj.service.service.StoreProductService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.service.dao.ArticleDao;
import com.zbkj.service.service.ArticleService;
import com.zbkj.service.service.CategoryService;
import com.zbkj.service.service.SystemAttachmentService;
import com.zbkj.service.service.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* ArticleServiceImpl 接口实现
*  +----------------------------------------------------------------------
 *  | CRMEB [ CRMEB赋能开发者，助力企业发展 ]
 *  +----------------------------------------------------------------------
 *  | Copyright (c) 2016~2025 https://www.crmeb.com All rights reserved.
 *  +----------------------------------------------------------------------
 *  | Licensed CRMEB并不是自由软件，未经许可不能去掉CRMEB相关版权
 *  +----------------------------------------------------------------------
 *  | Author: CRMEB Team <admin@crmeb.com>
 *  +----------------------------------------------------------------------
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {

    private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Resource
    private ArticleDao dao;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Autowired
    private ArticleCaseExtendService articleCaseExtendService;

    @Lazy
    @Autowired
    private StoreProductService storeProductService;

    /**
    * 列表
    * @param cid 文章分类id
    * @param pageParamRequest 分页类参数
    * @return PageInfo<Article>
    */
    @Override
    public PageInfo<ArticleResponse> getList(String cid, PageParamRequest pageParamRequest) {
        Page<Article> articlePage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(Article::getCid, cid);
        lambdaQueryWrapper.eq(Article::getHide, false);
        lambdaQueryWrapper.eq(Article::getStatus, true);
        lambdaQueryWrapper.orderByDesc(Article::getSort).orderByDesc(Article::getVisit).orderByDesc(Article::getCreateTime);
        List<Article> articleList = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(articleList)) {
            return CommonPage.copyPageInfo(articlePage, CollUtil.newArrayList());
        }
        List<ArticleResponse> responseList = articleList.stream().map(e -> {
            ArticleResponse articleResponse = new ArticleResponse();
            BeanUtils.copyProperties(e, articleResponse);
            return articleResponse;
        }).collect(Collectors.toList());
        return CommonPage.copyPageInfo(articlePage, responseList);
    }

    /**
     * 获取文章列表
     * @param request 请求参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    @Override
    public PageInfo<ArticleVo> getAdminList(ArticleSearchRequest request, PageParamRequest pageParamRequest) {
        List<Integer> caseArticleIds = null;
        if (StrUtil.isNotBlank(request.getIndustryCategory()) || StrUtil.isNotBlank(request.getRegionCategory())) {
            List<ArticleCaseExtend> caseExtends = articleCaseExtendService.listByCondition(
                    request.getIndustryCategory(), request.getRegionCategory());
            caseArticleIds = caseExtends.stream()
                    .map(e -> e.getArticleId().intValue())
                    .collect(Collectors.toList());
            if (caseArticleIds.isEmpty()) {
                return CommonPage.copyPageInfo(new Page<>(), new ArrayList<>());
            }
        }

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
        if (caseArticleIds != null) {
            lambdaQueryWrapper.in(Article::getId, caseArticleIds);
        }
        lambdaQueryWrapper.orderByDesc(Article::getVisit).orderByDesc(Article::getId);
        List<Article> articleList = dao.selectList(lambdaQueryWrapper);

        ArrayList<ArticleVo> articleVoArrayList = new ArrayList<>();
        if (articleList.size() < 1) {
            return CommonPage.copyPageInfo(articlePage, articleVoArrayList);
        }
        List<Long> articleIdList = articleList.stream().map(e -> Long.valueOf(e.getId())).collect(Collectors.toList());
        List<ArticleCaseExtend> allCaseExtends = articleCaseExtendService.listByArticleIds(articleIdList);
        for (Article article : articleList) {
            ArticleVo articleVo = new ArticleVo();
            BeanUtils.copyProperties(article, articleVo);
            if (!StrUtil.isBlank(article.getImageInput()) ) {
                articleVo.setImageInput(article.getImageInput());
            }
            ArticleCaseExtend caseExtend = allCaseExtends.stream()
                    .filter(ce -> ce.getArticleId().equals(Long.valueOf(article.getId())))
                    .findFirst().orElse(null);
            if (caseExtend != null) {
                articleVo.setProjectName(caseExtend.getProjectName());
                articleVo.setIndustryCategory(caseExtend.getIndustryCategory());
                articleVo.setRegionCategory(caseExtend.getRegionCategory());
            }
            articleVoArrayList.add(articleVo);
        }
        return CommonPage.copyPageInfo(articlePage, articleVoArrayList);
    }

    /**
     * 查询文章详情
     * @param id Integer
     * @return ArticleVo
     */
    @Override
    public ArticleResponse getVoByFront(Integer id) {
        Article article = getById(id);
        if (ObjectUtil.isNull(article)) {
            throw new CrmebException("文章不存在");
        }

        if (!article.getStatus()) {
            throw new CrmebException("文章不存在");
        }

        ArticleResponse articleResponse = new ArticleResponse();
        BeanUtils.copyProperties(article, articleResponse);

        String visit = StrUtil.isNotBlank(article.getVisit()) ? article.getVisit() : "0";
        int num = Integer.parseInt(visit) + 1;
        article.setVisit(String.valueOf(num));
        article.setUpdateTime(DateUtil.date());
        dao.updateById(article);
        return articleResponse;
    }

    /**
     * 获取移动端banner列表
     * @return List<Article>
     */
    @Override
    public List<Article> getBannerList() {
        // 根据配置控制banner的数量
        String articleBannerLimitString = systemConfigService.getValueByKey(Constants.ARTICLE_BANNER_LIMIT);
        int articleBannerLimit = Integer.parseInt(articleBannerLimitString);

        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(Article::getId, Article::getImageInput);
        lambdaQueryWrapper.eq(Article::getIsBanner, true);
        lambdaQueryWrapper.eq(Article::getHide, false);
        lambdaQueryWrapper.eq(Article::getStatus, true);
        lambdaQueryWrapper.orderByDesc(Article::getSort);
        lambdaQueryWrapper.last(" limit " + articleBannerLimit);
        return dao.selectList(lambdaQueryWrapper);
    }

    /**
     * 获取移动端热门列表
     * @return List<ArticleResponse>
     */
    @Override
    public List<ArticleResponse> getHotList() {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(Article::getId, Article::getImageInput, Article::getTitle, Article::getCreateTime);
        lambdaQueryWrapper.eq(Article::getIsHot, true);
        lambdaQueryWrapper.eq(Article::getHide, false);
        lambdaQueryWrapper.eq(Article::getStatus, true);
        lambdaQueryWrapper.orderByDesc(Article::getSort);
        lambdaQueryWrapper.last(" limit 20");
        List<Article> articleList = dao.selectList(lambdaQueryWrapper);
        if (CollUtil.isEmpty(articleList)) {
            return CollUtil.newArrayList();
        }
        List<ArticleResponse> responseList = articleList.stream().map(e -> {
            ArticleResponse articleResponse = new ArticleResponse();
            BeanUtils.copyProperties(e, articleResponse);
            return articleResponse;
        }).collect(Collectors.toList());
        return responseList;
    }

    /**
     * 获取文章分类列表(移动端)
     * @return List<Category>
     */
    @Override
    public List<Category> getCategoryList() {
        return categoryService.findArticleCategoryList();
    }

    /**
     * 文章新增
     * @param articleRequest 文章新增参数
     * @return Boolean
     */
    @Override
    public Boolean create(ArticleRequest articleRequest) {
        Article article = new Article();
        BeanUtils.copyProperties(articleRequest, article);
        article.setImageInput(systemAttachmentService.clearPrefix(article.getImageInput()));
        article.setContent(systemAttachmentService.clearPrefix(article.getContent()));
        article.setVisit("0");
        return save(article);
    }

    /**
     * 文章删除
     * @param id 文章id
     * @return Boolean
     */
    @Override
    public Boolean deleteById(Integer id) {
        Article article = getById(id);
        if (ObjectUtil.isNull(article)) {
            throw new CrmebException("文章已删除");
        }
        articleCaseExtendService.deleteByArticleId(Long.valueOf(id));
        return removeById(id);
    }

    /**
     * 文章修改
     * @param id 文章id
     * @param articleRequest 文章修改参数
     */
    @Override
    public Boolean updateArticle(Integer id, ArticleRequest articleRequest) {
        Article article = new Article();
        BeanUtils.copyProperties(articleRequest, article);
        article.setId(id);
        article.setImageInput(systemAttachmentService.clearPrefix(article.getImageInput()));
        article.setContent(systemAttachmentService.clearPrefix(article.getContent()));
        article.setUpdateTime(DateUtil.date());
        return updateById(article);
    }

    /**
     * 获取文章详情
     * @param id 文章id
     * @return Article
     */
    @Override
    public Article getDetail(Integer id) {
        Article article = getById(id);
        if (ObjectUtil.isNull(article)) {
            throw new CrmebException("文章不存在");
        }
        return article;
    }

    /**
     * 获取案例详情（包含扩展信息和关联商品）
     * @param id 文章ID
     * @return ArticleCaseDetailResponse
     */
    @Override
    public ArticleCaseDetailResponse getCaseDetail(Integer id) {
        Article article = getById(id);
        if (ObjectUtil.isNull(article)) {
            throw new CrmebException("文章不存在");
        }

        ArticleCaseDetailResponse response = new ArticleCaseDetailResponse();
        response.setId(article.getId());
        response.setTitle(article.getTitle());
        response.setImageInput(article.getImageInput());
        response.setSynopsis(article.getSynopsis());
        response.setContent(article.getContent());
        response.setCreateTime(DateUtil.format(article.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        response.setVisit(Integer.parseInt(StrUtil.isNotBlank(article.getVisit()) ? article.getVisit() : "0"));

        ArticleCaseExtend caseExtend = articleCaseExtendService.getByArticleId(Long.valueOf(id));
        if (caseExtend != null) {
            ArticleCaseExtendResponse caseExtendResponse = new ArticleCaseExtendResponse();
            BeanUtils.copyProperties(caseExtend, caseExtendResponse);
            response.setCaseInfo(caseExtendResponse);

            String productIds = caseExtend.getProductIds();
            if (StrUtil.isNotBlank(productIds)) {
                List<Integer> ids = Arrays.stream(productIds.replaceAll("[\\[\\]\"]", "").split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                if (CollUtil.isNotEmpty(ids)) {
                    List<ProductSimpleResponse> productList = storeProductService.getSimpleListByIds(ids);
                    response.setProductList(productList);
                }
            }
        }

        return response;
    }

    /**
     * 获取案例列表（支持行业/区域/类型筛选）
     * @param cid 分类ID
     * @param request 筛选参数
     * @param pageParamRequest 分页参数
     * @return PageInfo<ArticleCaseListResponse>
     */
    @Override
    public PageInfo<ArticleCaseListResponse> getCaseList(String cid, ArticleFrontSearchRequest request, PageParamRequest pageParamRequest) {
        Page<Article> articlePage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        LambdaQueryWrapper<Article> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(Article::getCid, cid);
        lambdaQueryWrapper.eq(Article::getHide, false);
        lambdaQueryWrapper.eq(Article::getStatus, true);
        lambdaQueryWrapper.orderByDesc(Article::getSort).orderByDesc(Article::getVisit).orderByDesc(Article::getCreateTime);
        List<Article> articleList = dao.selectList(lambdaQueryWrapper);

        if (CollUtil.isEmpty(articleList)) {
            return CommonPage.copyPageInfo(articlePage, CollUtil.newArrayList());
        }

        List<Long> articleIds = articleList.stream().map(e -> Long.valueOf(e.getId())).collect(Collectors.toList());
        List<ArticleCaseExtend> caseExtends = articleCaseExtendService.listByArticleIds(articleIds);

        if (request != null && (StrUtil.isNotBlank(request.getIndustryCategory())
                || StrUtil.isNotBlank(request.getRegionCategory())
                || StrUtil.isNotBlank(request.getProjectType()))) {

            if (CollUtil.isNotEmpty(caseExtends)) {
                List<Long> filteredIds = caseExtends.stream()
                        .filter(e -> {
                            if (StrUtil.isNotBlank(request.getIndustryCategory())
                                    && !request.getIndustryCategory().equals(e.getIndustryCategory())) {
                                return false;
                            }
                            if (StrUtil.isNotBlank(request.getRegionCategory())
                                    && !request.getRegionCategory().equals(e.getRegionCategory())) {
                                return false;
                            }
                            if (StrUtil.isNotBlank(request.getProjectType())
                                    && !request.getProjectType().equals(e.getProjectType())) {
                                return false;
                            }
                            return true;
                        })
                        .map(ArticleCaseExtend::getArticleId)
                        .collect(Collectors.toList());

                if (CollUtil.isEmpty(filteredIds)) {
                    return CommonPage.copyPageInfo(articlePage, CollUtil.newArrayList());
                }

                final List<Long> finalFilteredIds = filteredIds;
                articleList = articleList.stream()
                        .filter(a -> finalFilteredIds.contains(Long.valueOf(a.getId())))
                        .collect(Collectors.toList());
            } else {
                return CommonPage.copyPageInfo(articlePage, CollUtil.newArrayList());
            }
        }

        List<ArticleCaseListResponse> responseList = articleList.stream().map(e -> {
            ArticleCaseListResponse response = new ArticleCaseListResponse();
            response.setId(e.getId());
            response.setTitle(e.getTitle());
            response.setImageInput(e.getImageInput());
            response.setSynopsis(e.getSynopsis());

            ArticleCaseExtend caseExtend = caseExtends.stream()
                    .filter(ce -> ce.getArticleId().equals(Long.valueOf(e.getId())))
                    .findFirst().orElse(null);
            if (caseExtend != null) {
                ArticleCaseExtendResponse caseExtendResponse = new ArticleCaseExtendResponse();
                BeanUtils.copyProperties(caseExtend, caseExtendResponse);
                response.setCaseInfo(caseExtendResponse);
            }
            return response;
        }).collect(Collectors.toList());

        return CommonPage.copyPageInfo(articlePage, responseList);
    }

    /**
     * 保存案例扩展信息
     * @param articleId 文章ID
     * @param caseExtend 案例扩展信息
     * @return Boolean
     */
    @Override
    public Boolean saveCaseExtend(Integer articleId, ArticleCaseExtend caseExtend) {
        if (caseExtend == null) {
            return true;
        }
        caseExtend.setArticleId(Long.valueOf(articleId));
        caseExtend.setCreateTime(DateUtil.date());
        caseExtend.setUpdateTime(DateUtil.date());
        return articleCaseExtendService.save(caseExtend);
    }

    /**
     * 获取文章关联的商品ID列表
     * @param articleId 文章ID
     * @return String
     */
    @Override
    public String getProductIdsByArticleId(Integer articleId) {
        ArticleCaseExtend caseExtend = articleCaseExtendService.getByArticleId(Long.valueOf(articleId));
        if (caseExtend == null) {
            return "";
        }
        return StrUtil.isNotBlank(caseExtend.getProductIds()) ? caseExtend.getProductIds() : "";
    }

    @Override
    public Boolean createWithCase(ArticleRequestCaseExtend articleRequestCaseExtend) {
        Article article = new Article();
        BeanUtils.copyProperties(articleRequestCaseExtend, article);
        article.setImageInput(systemAttachmentService.clearPrefix(article.getImageInput()));
        article.setContent(systemAttachmentService.clearPrefix(article.getContent()));
        article.setVisit("0");
        boolean saved = save(article);
        if (!saved) {
            return false;
        }
        if (articleRequestCaseExtend.getCaseInfo() != null) {
            ArticleCaseExtend caseExtend = new ArticleCaseExtend();
            BeanUtils.copyProperties(articleRequestCaseExtend.getCaseInfo(), caseExtend);
            caseExtend.setArticleId(Long.valueOf(article.getId()));
            caseExtend.setCreateTime(DateUtil.date());
            caseExtend.setUpdateTime(DateUtil.date());
            articleCaseExtendService.save(caseExtend);
        }
        return true;
    }

    @Override
    public Boolean updateArticleWithCase(Integer id, ArticleRequestCaseExtendUpdate articleRequestCaseExtendUpdate) {
        Article article = new Article();
        BeanUtils.copyProperties(articleRequestCaseExtendUpdate, article);
        article.setId(id);
        article.setImageInput(systemAttachmentService.clearPrefix(article.getImageInput()));
        article.setContent(systemAttachmentService.clearPrefix(article.getContent()));
        article.setUpdateTime(DateUtil.date());
        boolean updated = updateById(article);
        if (!updated) {
            return false;
        }
        ArticleCaseExtendRequest caseInfo = articleRequestCaseExtendUpdate.getCaseInfo();
        if (caseInfo != null) {
            ArticleCaseExtend existing = articleCaseExtendService.getByArticleId(Long.valueOf(id));
            if (existing != null) {
                BeanUtils.copyProperties(caseInfo, existing);
                existing.setUpdateTime(DateUtil.date());
                articleCaseExtendService.update(existing);
            } else {
                ArticleCaseExtend caseExtend = new ArticleCaseExtend();
                BeanUtils.copyProperties(caseInfo, caseExtend);
                caseExtend.setArticleId(Long.valueOf(id));
                caseExtend.setCreateTime(DateUtil.date());
                caseExtend.setUpdateTime(DateUtil.date());
                articleCaseExtendService.save(caseExtend);
            }
        }
        return true;
    }

    @Override
    public Boolean bindProduct(Integer articleId, Integer productId) {
        ArticleCaseExtend caseExtend = articleCaseExtendService.getByArticleId(Long.valueOf(articleId));
        if (caseExtend == null) {
            caseExtend = new ArticleCaseExtend();
            caseExtend.setArticleId(Long.valueOf(articleId));
            caseExtend.setCreateTime(DateUtil.date());
            caseExtend.setUpdateTime(DateUtil.date());
            caseExtend.setProductIds("[" + productId + "]");
            return articleCaseExtendService.save(caseExtend);
        }
        String existingIds = caseExtend.getProductIds();
        List<Integer> idList = new ArrayList<>();
        if (StrUtil.isNotBlank(existingIds)) {
            idList = Arrays.stream(existingIds.replaceAll("[\\[\\]\"]", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        if (!idList.contains(productId)) {
            idList.add(productId);
        }
        caseExtend.setProductIds(idList.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]")));
        caseExtend.setUpdateTime(DateUtil.date());
        return articleCaseExtendService.update(caseExtend);
    }
}

