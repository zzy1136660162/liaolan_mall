package com.zbkj.front.controller;

import com.zbkj.common.request.ContactMessageRequest;
import com.zbkj.common.response.ContactInfoResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.ContactMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 联系我们
 */
@Slf4j
@RestController
@RequestMapping("api/front/contact")
@Api(tags = "联系我们")
public class ContactController {

    @Autowired
    private ContactMessageService contactMessageService;

    @ApiOperation(value = "获取联系方式信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<ContactInfoResponse> info() {
        return CommonResult.success(contactMessageService.getContactInfo());
    }

    @ApiOperation(value = "提交留言")
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public CommonResult<String> saveMessage(@RequestBody @Validated ContactMessageRequest request) {
        if (contactMessageService.submit(request)) {
            return CommonResult.success("提交成功");
        }
        return CommonResult.failed("提交失败");
    }
}
