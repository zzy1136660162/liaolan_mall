package com.zbkj.front.controller;

import com.zbkj.common.response.AboutPageBlockResponse;
import com.zbkj.common.result.CommonResult;
import com.zbkj.service.service.AboutPageBlockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台 - 关于辽缆控制器

 * 手机用户打开"关于辽缆"页面时调用，获取已启用的板块内容
 * URL前缀：api/front/about

 * 注意：@RestController("AboutFrontController") 传入bean名称，
 * 避免与 crmeb-admin 中的同命 Controller 冲突
 */
@Slf4j
@RestController("AboutFrontController")
@RequestMapping("api/front/about")
@Api(tags = "关于辽缆")
public class AboutController {

    @Autowired
    private AboutPageBlockService aboutPageBlockService;

    /**
     * 获取关于辽缆页面信息
     * 返回所有已启用的板块内容，按排序排列
     */
    @ApiOperation(value = "获取关于辽缆信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonResult<List<AboutPageBlockResponse>> getInfo() {
        return CommonResult.success(aboutPageBlockService.getEnabledList());
    }
}
