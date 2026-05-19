package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.model.download.DownloadMaterial;
import com.zbkj.common.model.download.DownloadMaterialRecord;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.DownloadMaterialRecordRequest;
import com.zbkj.common.request.DownloadMaterialRecordSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.DownloadMaterialRecordResponse;
import com.zbkj.service.dao.DownloadMaterialDao;
import com.zbkj.service.dao.DownloadMaterialRecordDao;
import com.zbkj.service.service.DownloadMaterialRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DownloadMaterialRecordServiceImpl extends ServiceImpl<DownloadMaterialRecordDao, DownloadMaterialRecord> implements DownloadMaterialRecordService {

    @Resource
    private DownloadMaterialRecordDao dao;

    @Resource
    private DownloadMaterialDao downloadMaterialDao;

    @Override
    public PageInfo<DownloadMaterialRecordResponse> getAdminList(DownloadMaterialRecordSearchRequest request, PageParamRequest pageParamRequest) {
        Page<DownloadMaterialRecord> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        LambdaQueryWrapper<DownloadMaterialRecord> wrapper = Wrappers.lambdaQuery();
        if (request.getMaterialId() != null) {
            wrapper.eq(DownloadMaterialRecord::getMaterialId, request.getMaterialId());
        }
        if (StrUtil.isNotBlank(request.getPhone())) {
            wrapper.like(DownloadMaterialRecord::getPhone, request.getPhone());
        }
        if (StrUtil.isNotBlank(request.getSourcePage())) {
            wrapper.like(DownloadMaterialRecord::getSourcePage, request.getSourcePage());
        }
        if (StrUtil.isNotBlank(request.getStartDate())) {
            wrapper.ge(DownloadMaterialRecord::getCreateTime, request.getStartDate() + " 00:00:00");
        }
        if (StrUtil.isNotBlank(request.getEndDate())) {
            wrapper.le(DownloadMaterialRecord::getCreateTime, request.getEndDate() + " 23:59:59");
        }
        wrapper.orderByDesc(DownloadMaterialRecord::getId);
        List<DownloadMaterialRecord> records = dao.selectList(wrapper);
        return CommonPage.copyPageInfo(page, convertResponseList(records));
    }

    @Override
    public Boolean saveRecord(DownloadMaterialRecordRequest request) {
        DownloadMaterialRecord record = new DownloadMaterialRecord();
        BeanUtils.copyProperties(request, record);
        record.setCreateTime(DateUtil.date());
        return save(record);
    }

    @Override
    public Boolean existsByMaterialIdAndPhone(Integer materialId, String phone) {
        LambdaQueryWrapper<DownloadMaterialRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DownloadMaterialRecord::getMaterialId, materialId);
        wrapper.eq(DownloadMaterialRecord::getPhone, phone);
        return count(wrapper) > 0;
    }

    private List<DownloadMaterialRecordResponse> convertResponseList(List<DownloadMaterialRecord> records) {
        if (CollUtil.isEmpty(records)) {
            return CollUtil.newArrayList();
        }
        List<Integer> materialIds = records.stream().map(DownloadMaterialRecord::getMaterialId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<DownloadMaterial> materialWrapper = Wrappers.lambdaQuery();
        materialWrapper.in(DownloadMaterial::getId, materialIds);
        List<DownloadMaterial> materials = downloadMaterialDao.selectList(materialWrapper);
        HashMap<Integer, String> materialMap = new HashMap<>();
        materials.forEach(item -> materialMap.put(item.getId(), item.getTitle()));
        return records.stream().map(record -> {
            DownloadMaterialRecordResponse response = new DownloadMaterialRecordResponse();
            BeanUtils.copyProperties(record, response);
            response.setMaterialTitle(materialMap.getOrDefault(record.getMaterialId(), ""));
            return response;
        }).collect(Collectors.toList());
    }
}
