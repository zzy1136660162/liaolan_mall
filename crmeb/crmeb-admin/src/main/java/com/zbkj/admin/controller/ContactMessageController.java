package com.zbkj.admin.controller;

import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.ContactConfigRequest;
import com.zbkj.common.request.ContactMessageHandleRequest;
import com.zbkj.common.request.ContactMessageSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.ContactInfoResponse;
import com.zbkj.common.response.ContactMessageResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.ContactMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 联系我们留言管理
 */
@Slf4j
@RestController
@RequestMapping("api/admin/contact/message")
@Api(tags = "联系我们管理")
public class ContactMessageController {

    @Autowired
    private ContactMessageService contactMessageService;

    @PreAuthorize("hasAuthority('admin:contact:message:list')")
    @ApiOperation(value = "留言分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResult<CommonPage<ContactMessageResponse>> getList(@Validated ContactMessageSearchRequest request,
                                                                    @Validated PageParamRequest pageParamRequest) {
        return CommonResult.success(CommonPage.restPage(contactMessageService.getAdminList(request, pageParamRequest)));
    }

    @PreAuthorize("hasAuthority('admin:contact:message:update')")
    @ApiOperation(value = "处理留言")
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    @ApiImplicitParam(name = "id", value = "留言ID")
    public CommonResult<String> handle(@RequestParam Integer id, @RequestBody @Validated ContactMessageHandleRequest request) {
        if (contactMessageService.handle(id, request)) {
            return CommonResult.success("处理成功");
        }
        return CommonResult.failed("处理失败");
    }

    @PreAuthorize("hasAuthority('admin:contact:config:info')")
    @ApiOperation(value = "获取联系方式配置")
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public CommonResult<ContactInfoResponse> getConfig() {
        return CommonResult.success(contactMessageService.getAdminConfig());
    }

    @PreAuthorize("hasAuthority('admin:contact:config:save')")
    @ApiOperation(value = "保存联系方式配置")
    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public CommonResult<String> saveConfig(@RequestBody @Validated ContactConfigRequest request) {
        if (contactMessageService.saveAdminConfig(request)) {
            return CommonResult.success("保存成功");
        }
        return CommonResult.failed("保存失败");
    }
}
