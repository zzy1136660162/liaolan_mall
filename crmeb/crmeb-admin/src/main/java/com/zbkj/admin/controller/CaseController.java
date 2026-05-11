package com.zbkj.admin.controller;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.CaseRequest;
import com.zbkj.common.request.CaseSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CaseDetailResponse;
import com.zbkj.common.response.CaseListResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.CaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 案例管理 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/admin/case")
@Api(tags = "案例管理")
public class CaseController {

    @Autowired
    private CaseService caseService;

    /**
     * 分页显示案例列表
     * @param request CaseSearchRequest 搜索条件
     * @param pageParamRequest 分页参数
     */
    @PreAuthorize("hasAuthority('admin:case:list')")
    @ApiOperation(value = "分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiImplicitParam(name="keywords", value="搜索关键字")
    public CommonResult<CommonPage<CaseListResponse>> getList(@Validated CaseSearchRequest request,
                                                                @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(caseService.getAdminList(request, pageParamRequest)));
    }

    /**
     * 新增案例
     * @param caseRequest 新增参数（扁平化）
     */
    @PreAuthorize("hasAuthority('admin:case:save')")
    @ApiOperation(value = "新增")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@RequestBody @Validated CaseRequest caseRequest) {
        if (caseService.create(caseRequest)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 删除案例
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:case:delete')")
    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="案例ID")
    public CommonResult<String> delete(@RequestParam(value = "id") Integer id) {
        if (caseService.delete(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 修改案例
     * @param id integer id
     * @param caseRequest 修改参数（扁平化）
     */
    @PreAuthorize("hasAuthority('admin:case:update')")
    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiImplicitParam(name="id", value="案例ID")
    public CommonResult<String> update(@RequestParam Integer id, @RequestBody @Validated CaseRequest caseRequest) {
        if (caseService.update(id, caseRequest)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    /**
     * 查询案例详情
     * @param id Integer
     */
    @PreAuthorize("hasAuthority('admin:case:info')")
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiImplicitParam(name="id", value="案例ID")
    public CommonResult<CaseDetailResponse> info(@RequestParam(value = "id") Integer id) {
        return CommonResult.success(caseService.getCaseDetail(id));
    }

    /**
     * 案例绑定商品
     * @param id 案例ID
     * @param productId 商品ID
     */
    @PreAuthorize("hasAuthority('admin:case:update')")
    @ApiOperation(value = "案例绑定商品")
    @RequestMapping(value = "/bind/product", method = RequestMethod.POST)
    @ApiImplicitParam(name="id", value="案例ID")
    public CommonResult<String> bindProduct(@RequestParam(value = "id") Integer id,
                                             @RequestParam(value = "productId") Integer productId) {
        if (caseService.bindProduct(id, productId)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }
}
