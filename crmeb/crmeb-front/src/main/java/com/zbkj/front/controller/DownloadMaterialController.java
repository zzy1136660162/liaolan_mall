package com.zbkj.front.controller;

import com.zbkj.common.model.category.Category;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.DownloadMaterialRecordRequest;
import com.zbkj.common.request.DownloadMaterialSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.DownloadMaterialResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.DownloadMaterialRecordService;
import com.zbkj.service.service.DownloadMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController("DownloadMaterialFrontController")
@RequestMapping("api/front/download/material")
@Api(tags = "资料下载前台")
public class DownloadMaterialController {

    @Autowired
    private DownloadMaterialService downloadMaterialService;

    @Autowired
    private DownloadMaterialRecordService downloadMaterialRecordService;

    @ApiOperation(value = "资料列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<DownloadMaterialResponse>> getList(@Validated DownloadMaterialSearchRequest request,
                                                                      @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(downloadMaterialService.getFrontList(request, pageParamRequest)));
    }

    @ApiOperation(value = "资料分类列表")
    @RequestMapping(value = "/category/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<Category>> getCategoryList() {
        return CommonResult.success(CommonPage.restPage(downloadMaterialService.getFrontCategoryList()));
    }

    @ApiOperation(value = "资料详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    @ApiImplicitParam(name = "id", value = "资料ID")
    public CommonResult<DownloadMaterialResponse> info(@PathVariable Integer id) {
        return CommonResult.success(downloadMaterialService.getFrontDetail(id));
    }

    @ApiOperation(value = "下载留资")
    @RequestMapping(value = "/record", method = RequestMethod.POST)
    public CommonResult<String> saveRecord(@RequestBody @Validated DownloadMaterialRecordRequest request) {
        downloadMaterialService.getFrontDetail(request.getMaterialId());
        return downloadMaterialRecordService.saveRecord(request) ? CommonResult.success() : CommonResult.failed();
    }

    @ApiOperation(value = "获取下载文件地址")
    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
    public CommonResult<String> getFileUrl(@PathVariable Integer id,
                                           @RequestParam(value = "phone", required = false) String phone) {
        return CommonResult.success(downloadMaterialService.getDownloadUrl(id, phone));
    }
}
