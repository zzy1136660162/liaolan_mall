package com.zbkj.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.about.AboutPageBlock;
import com.zbkj.common.request.AboutPageBlockRequest;
import com.zbkj.common.response.AboutPageBlockResponse;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.service.dao.AboutPageBlockDao;
import com.zbkj.service.service.AboutPageBlockService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 关于页内容块 Service 实现类

 * 这就是"大脑"，处理具体的业务逻辑
 */
@Service
public class AboutPageBlockServiceImpl extends ServiceImpl<AboutPageBlockDao, AboutPageBlock> implements AboutPageBlockService {

    @Resource
    private AboutPageBlockDao dao;

    /**
     * 获取所有内容块（后台管理用）
     * 按 sort 升序排列，包含停用的板块
     */
    @Override
    public List<AboutPageBlockResponse> getList() {
        LambdaQueryWrapper<AboutPageBlock> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AboutPageBlock::getSort);
        List<AboutPageBlock> list = dao.selectList(wrapper);
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * 获取已启用的内容块（前台展示用）
     * 只返回 status=1 的板块，按 sort 升序排列
     */
    @Override
    public List<AboutPageBlockResponse> getEnabledList() {
        LambdaQueryWrapper<AboutPageBlock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AboutPageBlock::getStatus, true);
        wrapper.orderByAsc(AboutPageBlock::getSort);
        List<AboutPageBlock> list = dao.selectList(wrapper);
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * 获取单个内容块详情
     */
    @Override
    public AboutPageBlockResponse getDetail(Integer id) {
        AboutPageBlock block = dao.selectById(id);
        if (block == null) {
            throw new CrmebException("内容块不存在");
        }
        return toResponse(block);
    }

    /**
     * 更新内容块
     * 管理员在后台编辑板块内容后调用
     */
    @Override
    public Boolean updateBlock(AboutPageBlockRequest request) {
        AboutPageBlock block = dao.selectById(request.getId());
        if (block == null) {
            throw new CrmebException("内容块不存在");
        }
        // 只更新允许修改的字段（blockKey 和 blockName 不允许改）
        if (request.getTitle() != null) {
            block.setTitle(request.getTitle());
        }
        if (request.getSubTitle() != null) {
            block.setSubTitle(request.getSubTitle());
        }
        if (request.getContent() != null) {
            block.setContent(request.getContent());
        }
        if (request.getImageUrls() != null) {
            block.setImageUrls(request.getImageUrls());
        }
        if (request.getSort() != null) {
            block.setSort(request.getSort());
        }
        if (request.getStatus() != null) {
            block.setStatus(request.getStatus());
        }
        int rows = dao.updateById(block);
        return rows > 0;
    }

    /**
     * 实体 → 响应对象 的转换方法
     * 把数据库查出来的 Model 转成前端需要的 Response 格式
     */
    private AboutPageBlockResponse toResponse(AboutPageBlock block) {
        AboutPageBlockResponse response = new AboutPageBlockResponse();
        BeanUtils.copyProperties(block, response);
        return response;
    }
}
