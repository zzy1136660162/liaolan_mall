package com.zbkj.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbkj.common.model.about.AboutPageBlock;
import com.zbkj.common.request.AboutPageBlockRequest;
import com.zbkj.common.response.AboutPageBlockResponse;

import java.util.List;

/**
 * 关于页内容块 Service 接口

 * 定义"关于辽缆"模块的业务方法
 */
public interface AboutPageBlockService extends IService<AboutPageBlock> {

    /**
     * 获取所有内容块（后台管理用，包含停用的）
     */
    List<AboutPageBlockResponse> getList();

    /**
     * 获取已启用的内容块（前台展示用，按排序排列）
     */
    List<AboutPageBlockResponse> getEnabledList();

    /**
     * 获取单个内容块详情
     * @param id 内容块ID
     */
    AboutPageBlockResponse getDetail(Integer id);

    /**
     * 更新内容块
     * @param request 修改请求体
     */
    Boolean updateBlock(AboutPageBlockRequest request);
}
