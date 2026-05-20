package com.zbkj.admin.controller;

import com.zbkj.common.request.AboutPageBlockRequest;
import com.zbkj.common.response.AboutPageBlockResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.AboutPageBlockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台管理 - 关于辽缆内容块控制器

 * 管理员在这里编辑"关于辽缆"页面的6个板块内容
 * URL前缀：api/admin/about/block
 */
@Slf4j
@RestController
@RequestMapping("api/admin/about/block")
@Api(tags = "关于辽缆内容块管理器")
public class AboutPageBlockController {

    @Autowired
    private AboutPageBlockService aboutPageBlockService;

    /**
     * 获取所有内容块列表
     * 管理后台打开"关于辽缆"页面时调用，显示6个板块
     */
    @PreAuthorize("hasAuthority('admin:about:block:list')")
    @ApiOperation(value = "内容块列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<List<AboutPageBlockResponse>> getList() {
        return CommonResult.success(aboutPageBlockService.getList());
    }

    /**
     * 获取单个内容块详情
     * 管理员点击"编辑"时调用，获取板块当前内容
     */
    @PreAuthorize("hasAuthority('admin:about:block:info')")
    @ApiOperation(value = "内容块详情")
    @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
    public CommonResult<AboutPageBlockResponse> getInfo(@PathVariable Integer id) {
        return CommonResult.success(aboutPageBlockService.getDetail(id));
    }

    /**
     * 更新内容块
     * 管理员编辑板块内容后点"保存"时调用
     */
    @PreAuthorize("hasAuthority('admin:about:block:update')")
    @ApiOperation(value = "更新内容块")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> update(@RequestBody @Validated AboutPageBlockRequest request) {
        if (aboutPageBlockService.updateBlock(request)) {
            return CommonResult.success("更新成功");
        }
        return CommonResult.failed("更新失败");
    }
}
