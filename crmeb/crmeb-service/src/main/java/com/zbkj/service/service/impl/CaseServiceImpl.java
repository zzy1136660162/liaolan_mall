package com.zbkj.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.common.model.article.Case;
import com.zbkj.common.page.CommonPage;
import com.zbkj.common.request.CaseRequest;
import com.zbkj.common.request.CaseSearchRequest;
import com.zbkj.common.request.PageParamRequest;
import com.zbkj.common.response.CaseDetailResponse;
import com.zbkj.common.response.CaseListResponse;
import com.zbkj.common.response.ProductSimpleResponse;
import com.zbkj.service.dao.CaseDao;
import com.zbkj.service.service.CaseService;
import com.zbkj.service.service.StoreProductService;
import com.zbkj.service.service.SystemAttachmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 案例服务实现
 */
@Service
public class CaseServiceImpl implements CaseService {

    private Logger logger = LoggerFactory.getLogger(CaseServiceImpl.class);

    @Resource
    private CaseDao dao;

    @Autowired
    private SystemAttachmentService systemAttachmentService;

    @Lazy
    @Autowired
    private StoreProductService storeProductService;

    @Override
    public CaseDetailResponse getCaseDetail(Integer id) {
        Case caseEntity = dao.selectById(id);
        if (ObjectUtil.isNull(caseEntity)) {
            throw new CrmebException("案例不存在");
        }

        CaseDetailResponse response = new CaseDetailResponse();
        BeanUtils.copyProperties(caseEntity, response);
        response.setCreateTime(DateUtil.format(caseEntity.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
        response.setVisit(Integer.parseInt(StrUtil.isNotBlank(caseEntity.getVisit()) ? caseEntity.getVisit() : "0"));

        String productIds = caseEntity.getProductIds();
        if (StrUtil.isNotBlank(productIds)) {
            List<Integer> ids = Arrays.stream(productIds.replaceAll("[\\[\\]\"]", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (CollUtil.isNotEmpty(ids)) {
                List<ProductSimpleResponse> productList = storeProductService.getSimpleListByIds(ids);
                response.setProductList(productList);
            }
        }

        return response;
    }

    @Override
    public PageInfo<CaseListResponse> getAdminList(CaseSearchRequest request, PageParamRequest pageParamRequest) {
        Page<Case> casePage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        LambdaQueryWrapper<Case> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(request.getKeywords())) {
            lambdaQueryWrapper.like(Case::getTitle, request.getKeywords());
        }
        if (StrUtil.isNotBlank(request.getIndustryCategory())) {
            lambdaQueryWrapper.eq(Case::getIndustryCategory, request.getIndustryCategory());
        }
        if (StrUtil.isNotBlank(request.getRegionCategory())) {
            lambdaQueryWrapper.eq(Case::getRegionCategory, request.getRegionCategory());
        }
        if (StrUtil.isNotBlank(request.getProjectType())) {
            lambdaQueryWrapper.eq(Case::getProjectType, request.getProjectType());
        }
        if (request.getStatus() != null) {
            lambdaQueryWrapper.eq(Case::getStatus, request.getStatus());
        }
        lambdaQueryWrapper.orderByDesc(Case::getSort).orderByDesc(Case::getCreateTime);
        List<Case> caseList = dao.selectList(lambdaQueryWrapper);

        List<CaseListResponse> responseList = caseList.stream().map(e -> {
            CaseListResponse response = new CaseListResponse();
            response.setId(e.getId());
            response.setTitle(e.getTitle());
            response.setAuthor(e.getAuthor());
            response.setImageInput(e.getImageInput());
            response.setSynopsis(e.getSynopsis());
            response.setIndustryCategory(e.getIndustryCategory());
            response.setRegionCategory(e.getRegionCategory());
            response.setProjectType(e.getProjectType());
            response.setProjectName(e.getProjectName());
            response.setVisit(Integer.parseInt(StrUtil.isNotBlank(e.getVisit()) ? e.getVisit() : "0"));
            response.setUpdateTime(DateUtil.format(e.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
            return response;
        }).collect(Collectors.toList());

        return CommonPage.copyPageInfo(casePage, responseList);
    }

    @Override
    public PageInfo<CaseListResponse> getFrontList(CaseSearchRequest request, PageParamRequest pageParamRequest) {
        Page<Case> casePage = PageHelper.startPage(pageParamRequest.getPage(), pageParamRequest.getLimit());

        LambdaQueryWrapper<Case> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(Case::getHide, false);
        lambdaQueryWrapper.eq(Case::getStatus, true);
        if (StrUtil.isNotBlank(request.getIndustryCategory())) {
            lambdaQueryWrapper.eq(Case::getIndustryCategory, request.getIndustryCategory());
        }
        if (StrUtil.isNotBlank(request.getRegionCategory())) {
            lambdaQueryWrapper.eq(Case::getRegionCategory, request.getRegionCategory());
        }
        if (StrUtil.isNotBlank(request.getProjectType())) {
            lambdaQueryWrapper.eq(Case::getProjectType, request.getProjectType());
        }
        lambdaQueryWrapper.orderByDesc(Case::getSort).orderByDesc(Case::getCreateTime);
        List<Case> caseList = dao.selectList(lambdaQueryWrapper);

        List<CaseListResponse> responseList = caseList.stream().map(e -> {
            CaseListResponse response = new CaseListResponse();
            response.setId(e.getId());
            response.setTitle(e.getTitle());
            response.setAuthor(e.getAuthor());
            response.setImageInput(e.getImageInput());
            response.setSynopsis(e.getSynopsis());
            response.setIndustryCategory(e.getIndustryCategory());
            response.setRegionCategory(e.getRegionCategory());
            response.setProjectType(e.getProjectType());
            response.setProjectName(e.getProjectName());
            response.setVisit(Integer.parseInt(StrUtil.isNotBlank(e.getVisit()) ? e.getVisit() : "0"));
            return response;
        }).collect(Collectors.toList());

        return CommonPage.copyPageInfo(casePage, responseList);
    }

    @Override
    public Boolean create(CaseRequest request) {
        Case caseEntity = new Case();
        BeanUtils.copyProperties(request, caseEntity);
        caseEntity.setImageInput(systemAttachmentService.clearPrefix(caseEntity.getImageInput()));
        if (StrUtil.isNotBlank(caseEntity.getContent())) {
            caseEntity.setContent(systemAttachmentService.clearPrefix(caseEntity.getContent()));
        }
        caseEntity.setVisit("0");
        caseEntity.setCreateTime(DateUtil.date());
        caseEntity.setUpdateTime(DateUtil.date());
        return dao.insert(caseEntity) > 0;
    }

    @Override
    public Boolean update(Integer id, CaseRequest request) {
        Case caseEntity = dao.selectById(id);
        if (ObjectUtil.isNull(caseEntity)) {
            throw new CrmebException("案例不存在");
        }
        BeanUtils.copyProperties(request, caseEntity);
        caseEntity.setId(id);
        caseEntity.setImageInput(systemAttachmentService.clearPrefix(caseEntity.getImageInput()));
        if (StrUtil.isNotBlank(caseEntity.getContent())) {
            caseEntity.setContent(systemAttachmentService.clearPrefix(caseEntity.getContent()));
        }
        caseEntity.setUpdateTime(DateUtil.date());
        return dao.updateById(caseEntity) > 0;
    }

    @Override
    public Boolean delete(Integer id) {
        Case caseEntity = dao.selectById(id);
        if (ObjectUtil.isNull(caseEntity)) {
            throw new CrmebException("案例不存在");
        }
        return dao.deleteById(id) > 0;
    }

    @Override
    public Boolean bindProduct(Integer id, Integer productId) {
        Case caseEntity = dao.selectById(id);
        if (ObjectUtil.isNull(caseEntity)) {
            throw new CrmebException("案例不存在");
        }
        String existingIds = caseEntity.getProductIds();
        List<Integer> idList = new ArrayList<>();
        if (StrUtil.isNotBlank(existingIds)) {
            idList = Arrays.stream(existingIds.replaceAll("[\\[\\]\"]", "").split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        if (!idList.contains(productId)) {
            idList.add(productId);
        }
        caseEntity.setProductIds(idList.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]")));
        caseEntity.setUpdateTime(DateUtil.date());
        return dao.updateById(caseEntity) > 0;
    }

    @Override
    public List<String> getDistinctIndustryCategories() {
        LambdaQueryWrapper<Case> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.select(Case::getIndustryCategory);
        lambdaQueryWrapper.isNotNull(Case::getIndustryCategory);
        lambdaQueryWrapper.ne(Case::getIndustryCategory, "");
        List<Case> caseList = dao.selectList(lambdaQueryWrapper);
        return caseList.stream()
                .map(Case::getIndustryCategory)
                .distinct()
                .collect(Collectors.toList());
    }
}
