package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.category.Category;
import com.zbkj.common.model.download.DownloadMaterial;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.CategorySearchRequest;
import com.zbkj.common.request.DownloadMaterialRequest;
import com.zbkj.common.request.DownloadMaterialSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.DownloadMaterialResponse;
import com.zbkj.service.dao.DownloadMaterialDao;
import com.zbkj.service.service.CategoryService;
import com.zbkj.service.service.DownloadMaterialRecordService;
import com.zbkj.service.service.DownloadMaterialService;
import com.zbkj.service.service.SystemAttachmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DownloadMaterialServiceImpl extends ServiceImpl<DownloadMaterialDao, DownloadMaterial> implements DownloadMaterialService {

    @Resource
    private DownloadMaterialDao dao;

    @Autowired
    private DownloadMaterialRecordService downloadMaterialRecordService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Override
    public PageInfo<DownloadMaterialResponse> getAdminList(DownloadMaterialSearchRequest request, PageParamRequest pageParamRequest) {
        Page<DownloadMaterial> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<DownloadMaterial> materials = dao.selectList(buildAdminWrapper(request));
        return CommonPage.copyPageInfo(page, convertResponseList(materials));
    }

    @Override
    public Boolean create(DownloadMaterialRequest request) {
        DownloadMaterial material = new DownloadMaterial();
        BeanUtils.copyProperties(request, material);
        normalizeMaterial(material);
        material.setDownloadCount(0);
        material.setCreateTime(DateUtil.date());
        material.setUpdateTime(DateUtil.date());
        return save(material);
    }

    @Override
    public Boolean updateMaterial(Integer id, DownloadMaterialRequest request) {
        DownloadMaterial material = getById(id);
        if (ObjectUtil.isNull(material)) {
            throw new CrmebException("资料不存在");
        }
        BeanUtils.copyProperties(request, material);
        material.setId(id);
        normalizeMaterial(material);
        material.setUpdateTime(DateUtil.date());
        return updateById(material);
    }

    @Override
    public DownloadMaterialResponse getAdminDetail(Integer id) {
        DownloadMaterial material = getById(id);
        if (ObjectUtil.isNull(material)) {
            throw new CrmebException("资料不存在");
        }
        return convertResponse(material, buildCategoryMap(CollUtil.newArrayList(material.getCategoryId())));
    }

    @Override
    public PageInfo<DownloadMaterialResponse> getFrontList(DownloadMaterialSearchRequest request, PageParamRequest pageParamRequest) {
        Page<DownloadMaterial> page = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());
        List<DownloadMaterial> materials = dao.selectList(buildFrontWrapper(request));
        return CommonPage.copyPageInfo(page, convertResponseList(materials));
    }

    @Override
    public DownloadMaterialResponse getFrontDetail(Integer id) {
        LambdaQueryWrapper<DownloadMaterial> wrapper = buildFrontWrapper(new DownloadMaterialSearchRequest());
        wrapper.eq(DownloadMaterial::getId, id);
        DownloadMaterial material = dao.selectOne(wrapper);
        if (ObjectUtil.isNull(material)) {
            throw new CrmebException("资料不存在");
        }
        return convertResponse(material, buildCategoryMap(CollUtil.newArrayList(material.getCategoryId())));
    }

    @Override
    public String getDownloadUrl(Integer id, String phone) {
        DownloadMaterial material = getById(id);
        if (ObjectUtil.isNull(material) || !Boolean.TRUE.equals(material.getStatus()) || !Boolean.TRUE.equals(material.getIsPublic())) {
            throw new CrmebException("资料不存在或已停用");
        }
        if (Boolean.TRUE.equals(material.getNeedForm())) {
            if (StrUtil.isBlank(phone)) {
                throw new CrmebException("请先提交下载信息");
            }
            if (!downloadMaterialRecordService.existsByMaterialIdAndPhone(id, phone)) {
                throw new CrmebException("请先提交下载信息");
            }
        }
        material.setDownloadCount((material.getDownloadCount() == null ? 0 : material.getDownloadCount()) + 1);
        material.setUpdateTime(DateUtil.date());
        dao.updateById(material);
        return material.getFileUrl();
    }

    @Override
    public List<Category> getFrontCategoryList() {
        CategorySearchRequest request = new CategorySearchRequest();
        request.setType(2);
        request.setStatus(1);
        return categoryService.getList(request);
    }

    private LambdaQueryWrapper<DownloadMaterial> buildAdminWrapper(DownloadMaterialSearchRequest request) {
        LambdaQueryWrapper<DownloadMaterial> wrapper = Wrappers.lambdaQuery();
        if (ObjectUtil.isNotNull(request.getCategoryId())) {
            wrapper.eq(DownloadMaterial::getCategoryId, request.getCategoryId());
        }
        if (StrUtil.isNotBlank(request.getKeywords())) {
            wrapper.and(i -> i.like(DownloadMaterial::getTitle, request.getKeywords())
                    .or().like(DownloadMaterial::getDescription, request.getKeywords())
                    .or().like(DownloadMaterial::getFileType, request.getKeywords()));
        }
        if (ObjectUtil.isNotNull(request.getStatus()) && request.getStatus() >= 0) {
            wrapper.eq(DownloadMaterial::getStatus, request.getStatus() == 1);
        }
        wrapper.orderByDesc(DownloadMaterial::getSort).orderByDesc(DownloadMaterial::getId);
        return wrapper;
    }

    private LambdaQueryWrapper<DownloadMaterial> buildFrontWrapper(DownloadMaterialSearchRequest request) {
        LambdaQueryWrapper<DownloadMaterial> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DownloadMaterial::getStatus, true);
        wrapper.eq(DownloadMaterial::getIsPublic, true);
        if (ObjectUtil.isNotNull(request.getCategoryId()) && request.getCategoryId() > 0) {
            wrapper.eq(DownloadMaterial::getCategoryId, request.getCategoryId());
        }
        if (StrUtil.isNotBlank(request.getKeywords())) {
            wrapper.and(i -> i.like(DownloadMaterial::getTitle, request.getKeywords())
                    .or().like(DownloadMaterial::getDescription, request.getKeywords())
                    .or().like(DownloadMaterial::getFileType, request.getKeywords()));
        }
        wrapper.orderByDesc(DownloadMaterial::getSort).orderByDesc(DownloadMaterial::getId);
        return wrapper;
    }

    private void normalizeMaterial(DownloadMaterial material) {
        material.setFileUrl(systemAttachmentService.clearPrefix(material.getFileUrl()));
        if (StrUtil.isNotBlank(material.getCoverImage())) {
            material.setCoverImage(systemAttachmentService.clearPrefix(material.getCoverImage()));
        }
        material.setFileType(inferFileType(material.getFileUrl(), material.getFileType()));
        if (ObjectUtil.isNull(material.getSort())) {
            material.setSort(0);
        }
    }

    private String inferFileType(String fileUrl, String fileType) {
        if (StrUtil.isNotBlank(fileType)) {
            return fileType.toLowerCase(Locale.ROOT);
        }
        if (StrUtil.isBlank(fileUrl) || !fileUrl.contains(".")) {
            return "";
        }
        return fileUrl.substring(fileUrl.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private List<DownloadMaterialResponse> convertResponseList(List<DownloadMaterial> materials) {
        if (CollUtil.isEmpty(materials)) {
            return CollUtil.newArrayList();
        }
        List<Integer> categoryIds = materials.stream().map(DownloadMaterial::getCategoryId).distinct().collect(Collectors.toList());
        HashMap<Integer, String> categoryMap = buildCategoryMap(categoryIds);
        return materials.stream().map(item -> convertResponse(item, categoryMap)).collect(Collectors.toList());
    }

    private DownloadMaterialResponse convertResponse(DownloadMaterial material, HashMap<Integer, String> categoryMap) {
        DownloadMaterialResponse response = new DownloadMaterialResponse();
        BeanUtils.copyProperties(material, response);
        response.setCategoryName(categoryMap.getOrDefault(material.getCategoryId(), ""));
        return response;
    }

    private HashMap<Integer, String> buildCategoryMap(List<Integer> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return new HashMap<>();
        }
        return categoryService.getListInId(categoryIds);
    }
}
