package com.zbkj.front.controller;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.CaseSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CaseDetailResponse;
import com.zbkj.common.response.CaseListResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.CaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 案例
 */
@Slf4j
@RestController("CaseFrontController")
@RequestMapping("api/front/case")
@Api(tags = "案例")
public class CaseController {

    @Autowired
    private CaseService caseService;

    /**
     * 案例列表（支持行业/区域/类型筛选）
     * @param request 筛选参数
     * @param pageParamRequest 分页参数
     */
    @ApiOperation(value = "案例列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<CaseListResponse>> getCaseList(
            CaseSearchRequest request,
            @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(caseService.getFrontList(request, pageParamRequest)));
    }

    /**
     * 查询案例详情（包含扩展信息和关联商品）
     * @param id Integer
     */
    @ApiOperation(value = "案例详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="案例ID")
    public CommonResult<CaseDetailResponse> caseInfo(@RequestParam(value = "id") Integer id) {
        return CommonResult.success(caseService.getCaseDetail(id));
    }

    /**
     * 获取案例行业分类列表（去重）
     */
    @ApiOperation(value = "案例行业分类列表")
    @RequestMapping(value = "/industry/categories", method = RequestMethod.GET)
    public CommonResult<List<String>> getCaseIndustryCategories() {
        return CommonResult.success(caseService.getDistinctIndustryCategories());
    }
}
