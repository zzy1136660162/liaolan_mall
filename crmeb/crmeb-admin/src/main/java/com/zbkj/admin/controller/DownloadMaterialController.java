package com.zbkj.admin.controller;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.DownloadMaterialRecordSearchRequest;
import com.zbkj.common.request.DownloadMaterialRequest;
import com.zbkj.common.request.DownloadMaterialSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.DownloadMaterialRecordResponse;
import com.zbkj.common.response.DownloadMaterialResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.DownloadMaterialRecordService;
import com.zbkj.service.service.DownloadMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/admin/download/material")
@Api(tags = "资料下载管理")
public class DownloadMaterialController {

    @Autowired
    private DownloadMaterialService downloadMaterialService;

    @Autowired
    private DownloadMaterialRecordService downloadMaterialRecordService;

    @PreAuthorize("hasAuthority('admin:download:material:list')")
    @ApiOperation(value = "资料列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<DownloadMaterialResponse>> getList(@Validated DownloadMaterialSearchRequest request,
                                                                      @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(downloadMaterialService.getAdminList(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('admin:download:material:save')")
    @ApiOperation(value = "新增资料")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResult<String> save(@RequestBody @Validated DownloadMaterialRequest request) {
        return downloadMaterialService.create(request) ? CommonResult.success() : CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('admin:download:material:update')")
    @ApiOperation(value = "更新资料")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestParam Integer id, @RequestBody @Validated DownloadMaterialRequest request) {
        return downloadMaterialService.updateMaterial(id, request) ? CommonResult.success() : CommonResult.failed();
    }

    @PreAuthorize("hasAuthority('admin:download:material:info')")
    @ApiOperation(value = "资料详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "id", value = "资料ID")
    public CommonResult<DownloadMaterialResponse> info(@PathVariable Integer id) {
        return CommonResult.success(downloadMaterialService.getAdminDetail(id));
    }

    @PreAuthorize("hasAuthority('admin:download:material:record:list')")
    @ApiOperation(value = "下载记录列表")
    @RequestMapping(value = "/record/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<DownloadMaterialRecordResponse>> getRecordList(@Validated DownloadMaterialRecordSearchRequest request,
                                                                                   @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(downloadMaterialRecordService.getAdminList(request, pageParamRequest)));
    }
}
