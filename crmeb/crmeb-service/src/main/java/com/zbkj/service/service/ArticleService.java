package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.article.Article;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.request.ArticleRequest;
import com.zbkj.common.request.ArticleRequestCaseExtend;
import com.zbkj.common.request.ArticleRequestCaseExtendUpdate;
import com.zbkj.common.request.ArticleSearchRequest;
import com.zbkj.common.request.ArticleFrontSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ArticleCaseDetailResponse;
import com.zbkj.common.response.ArticleCaseListResponse;
import com.zbkj.common.response.ArticleResponse;
import com.zbkj.common.vo.ArticleVo;
import com.zbkj.common.model.article.ArticleCaseExtend;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
*  ArticleService 接口
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
public interface ArticleService extends IService<Article> {

    /**
     * 文章列表
     * @param cid 文章分类id
     * @param pageParamRequest 分页类参数
     * @return PageInfo<Article>
     */
    PageInfo<ArticleResponse> getList(String cid, PageParamRequest pageParamRequest);

    /**
     * 获取文章列表
     * @param request 请求参数
     * @param pageParamRequest 分页参数
     * @return PageInfo
     */
    PageInfo<ArticleVo> getAdminList(ArticleSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 文章详情
     * @param id 文章id
     * @return ArticleVo
     */
    ArticleResponse getVoByFront(Integer id);

    /**
     * 获取移动端banner列表
     * @return List<Article>
     */
    List<Article> getBannerList();

    /**
     * 获取移动端热门列表
     * @return List<ArticleResponse>
     */
    List<ArticleResponse> getHotList();

    /**
     * 获取文章分类列表
     * @return List<Category>
     */
    List<Category> getCategoryList();

    /**
     * 文章新增
     * @param articleRequest 文章新增参数
     * @return Boolean
     */
    Boolean create(ArticleRequest articleRequest);

    /**
     * 文章删除
     * @param id 文章id
     * @return Boolean
     */
    Boolean deleteById(Integer id);

    /**
     * 文章修改
     * @param id 文章id
     * @param articleRequest 文章修改参数
     */
    Boolean updateArticle(Integer id, ArticleRequest articleRequest);

    /**
     * 获取文章详情
     * @param id 文章id
     * @return Article
     */
    Article getDetail(Integer id);

    /**
     * 获取案例详情（包含扩展信息和关联商品）
     * @param id 文章ID
     * @return ArticleCaseDetailResponse
     */
    ArticleCaseDetailResponse getCaseDetail(Integer id);

    /**
     * 获取案例列表（支持行业/区域/类型筛选）
     * @param cid 分类ID
     * @param request 筛选参数
     * @param pageParamRequest 分页参数
     * @return PageInfo<ArticleCaseListResponse>
     */
    PageInfo<ArticleCaseListResponse> getCaseList(String cid, ArticleFrontSearchRequest request, PageParamRequest pageParamRequest);

    /**
     * 保存案例扩展信息
     * @param articleId 文章ID
     * @param caseExtend 案例扩展信息
     * @return Boolean
     */
    Boolean saveCaseExtend(Integer articleId, ArticleCaseExtend caseExtend);

    /**
     * 获取文章关联的商品ID列表
     * @param articleId 文章ID
     * @return String
     */
    String getProductIdsByArticleId(Integer articleId);

    /**
     * 新增文章（含案例扩展信息）
     * @param articleRequestCaseExtend 文章新增参数（含案例扩展）
     * @return Boolean
     */
    Boolean createWithCase(ArticleRequestCaseExtend articleRequestCaseExtend);

    /**
     * 文章修改（含案例扩展信息）
     * @param id 文章id
     * @param articleRequestCaseExtend 文章修改参数（含案例扩展）
     * @return Boolean
     */
    Boolean updateArticleWithCase(Integer id, ArticleRequestCaseExtendUpdate articleRequestCaseExtendUpdate);

    /**
     * 绑定商品到案例
     * @param articleId 文章ID
     * @param productId 商品ID
     * @return Boolean
     */
    Boolean bindProduct(Integer articleId, Integer productId);
}
