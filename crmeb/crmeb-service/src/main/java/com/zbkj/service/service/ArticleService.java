package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.article.Article;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.request.ArticleRequest;
import com.zbkj.common.request.ArticleSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ArticleResponse;
import com.zbkj.common.vo.ArticleVo;

import java.util.List;

/**
 * Article service interface.
 */
public interface ArticleService extends IService<Article> {

    /**
     * Front article list by category.
     */
    PageInfo<ArticleResponse> getList(String cid, PageParamRequest pageParamRequest);

    /**
     * Admin article list.
     */
    PageInfo<ArticleVo> getAdminList(ArticleSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * Front article detail with visit increment.
     */
    ArticleResponse getVoByFront(Integer id);

    /**
     * Mobile banner article list.
     */
    List<Article> getBannerList();

    /**
     * Mobile hot article list.
     */
    List<ArticleResponse> getHotList();

    /**
     * Article category list.
     */
    List<Category> getCategoryList();

    /**
     * Create article.
     */
    Boolean create(ArticleRequest articleRequest);

    /**
     * Delete article by id.
     */
    Boolean deleteById(Integer id);

    /**
     * Update article by id.
     */
    Boolean updateArticle(Integer id, ArticleRequest articleRequest);

    /**
     * Admin article detail.
     */
    ArticleResponse getDetail(Integer id);

    /**
     * Bind product to article.
     */
    Boolean bindProduct(Integer id, Integer productId);
}
