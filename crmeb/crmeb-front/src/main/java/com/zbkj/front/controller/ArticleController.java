package com.zbkj.front.controller;

import com.zbkj.common.model.article.Article;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ArticleFrontSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ArticleCaseDetailResponse;
import com.zbkj.common.response.ArticleCaseListResponse;
import com.zbkj.common.response.ArticleResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.ArticleCaseExtendService;
import com.zbkj.service.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 文章
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
@Slf4j
@RestController("ArticleFrontController")
@RequestMapping("api/front/article")
@Api(tags = "文章")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleCaseExtendService articleCaseExtendService;

    /**
     * 分页列表
     */
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    public CommonResult<CommonPage<ArticleResponse>> getList(@PathVariable(name="cid") String cid,
                                                             @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(articleService.getList(cid, pageParamRequest)));
    }

    /**
     * 热门列表
     */
    @ApiOperation(value = "热门列表")
    @RequestMapping(value = "/hot/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<ArticleResponse>> getHotList() {
        return CommonResult.success(CommonPage.restPage(articleService.getHotList()));
    }

    /**
     * 轮播列表
     */
    @ApiOperation(value = "轮播列表")
    @RequestMapping(value = "/banner/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<Article>> getBannerList() {
        return CommonResult.success(CommonPage.restPage(articleService.getBannerList()));
    }

    /**
     * 文章分类列表
     */
    @ApiOperation(value = "文章分类列表")
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<Category>> categoryList() {
        return CommonResult.success(CommonPage.restPage(articleService.getCategoryList()));
    }

    /**
     * 查询文章详情
     * @param id Integer
     */
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="文章ID")
    public CommonResult<ArticleResponse> info(@RequestParam(value = "id") Integer id) {
        return CommonResult.success(articleService.getVoByFront(id));
    }

    /**
     * 案例列表（支持行业/区域/类型筛选）
     * @param cid 分类ID
     * @param request 筛选参数
     * @param pageParamRequest 分页参数
     */
    @ApiOperation(value = "案例列表")
    @RequestMapping(value = "/case/list/{cid}", method = RequestMethod.GET)
    @ApiImplicitParam(name="cid", value="分类ID")
    public CommonResult<CommonPage<ArticleCaseListResponse>> getCaseList(
            @PathVariable(name="cid") String cid,
            ArticleFrontSearchRequest request,
            @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(articleService.getCaseList(cid, request, pageParamRequest)));
    }

    /**
     * 查询案例详情（包含扩展信息和关联商品）
     * @param id Integer
     */
    @ApiOperation(value = "案例详情")
    @RequestMapping(value = "/case/info", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="文章ID")
    public CommonResult<ArticleCaseDetailResponse> caseInfo(@RequestParam(value = "id") Integer id) {
        return CommonResult.success(articleService.getCaseDetail(id));
    }

    /**
     * 获取案例行业分类列表（去重）
     */
    @ApiOperation(value = "案例行业分类列表")
    @RequestMapping(value = "/case/industry/categories", method = RequestMethod.GET)
    public CommonResult<List<String>> getCaseIndustryCategories() {
        return CommonResult.success(articleCaseExtendService.getDistinctIndustryCategories());
    }
}
