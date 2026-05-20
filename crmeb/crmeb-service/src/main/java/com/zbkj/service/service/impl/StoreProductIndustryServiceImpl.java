package com.zbkj.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbkj.common.model.product.StoreProductIndustry;
import com.zbkj.common.request.ProductIndustryRequest;
import com.zbkj.common.response.ProductIndustryResponse;
import com.zbkj.service.dao.StoreProductIndustryDao;
import com.zbkj.service.service.StoreProductIndustryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;

@Service
public class StoreProductIndustryServiceImpl extends ServiceImpl<StoreProductIndustryDao, StoreProductIndustry>
        implements StoreProductIndustryService {

    @Override
    public void saveIndustryInfo(Integer productId, ProductIndustryRequest request) {
        StoreProductIndustry industry = new StoreProductIndustry();
        industry.setProductId(productId);
        industry.setModelNo(request.getModelNo() != null ? request.getModelNo() : "");
        industry.setVoltageLevel(request.getVoltageLevel() != null ? request.getVoltageLevel() : "");
        industry.setConductorMaterial(request.getConductorMaterial() != null ? request.getConductorMaterial() : "");
        industry.setCoreCount(request.getCoreCount() != null ? request.getCoreCount() : "");
        industry.setCrossSectionArea(request.getCrossSectionArea() != null ? request.getCrossSectionArea() : "");
        industry.setSheathMaterial(request.getSheathMaterial() != null ? request.getSheathMaterial() : "");
        industry.setFlameRetardantGrade(request.getFlameRetardantGrade() != null ? request.getFlameRetardantGrade() : "");
        industry.setStandardCode(request.getStandardCode() != null ? request.getStandardCode() : "");
        industry.setApplicationScene(request.getApplicationScene() != null ? request.getApplicationScene() : "");
        industry.setCertificateFiles(!CollectionUtils.isEmpty(request.getCertificateFiles()) ? JSON.toJSONString(request.getCertificateFiles()) : "[]");
        industry.setDownloadFileIds(!CollectionUtils.isEmpty(request.getDownloadFileIds()) ? JSON.toJSONString(request.getDownloadFileIds()) : "[]");
        industry.setCreateTime(new Date());
        industry.setUpdateTime(new Date());
        save(industry);
    }

    @Override
    public void updateIndustryInfo(Integer productId, ProductIndustryRequest request) {
        StoreProductIndustry existing = getByProductId(productId);
        if (existing == null) {
            saveIndustryInfo(productId, request);
            return;
        }
        existing.setModelNo(request.getModelNo() != null ? request.getModelNo() : "");
        existing.setVoltageLevel(request.getVoltageLevel() != null ? request.getVoltageLevel() : "");
        existing.setConductorMaterial(request.getConductorMaterial() != null ? request.getConductorMaterial() : "");
        existing.setCoreCount(request.getCoreCount() != null ? request.getCoreCount() : "");
        existing.setCrossSectionArea(request.getCrossSectionArea() != null ? request.getCrossSectionArea() : "");
        existing.setSheathMaterial(request.getSheathMaterial() != null ? request.getSheathMaterial() : "");
        existing.setFlameRetardantGrade(request.getFlameRetardantGrade() != null ? request.getFlameRetardantGrade() : "");
        existing.setStandardCode(request.getStandardCode() != null ? request.getStandardCode() : "");
        existing.setApplicationScene(request.getApplicationScene() != null ? request.getApplicationScene() : "");
        existing.setCertificateFiles(!CollectionUtils.isEmpty(request.getCertificateFiles()) ? JSON.toJSONString(request.getCertificateFiles()) : "[]");
        existing.setDownloadFileIds(!CollectionUtils.isEmpty(request.getDownloadFileIds()) ? JSON.toJSONString(request.getDownloadFileIds()) : "[]");
        existing.setUpdateTime(new Date());
        updateById(existing);
    }

    @Override
    public ProductIndustryResponse getIndustryInfo(Integer productId) {
        StoreProductIndustry industry = getByProductId(productId);
        if (industry == null) {
            return buildEmptyResponse();
        }
        ProductIndustryResponse response = new ProductIndustryResponse();
        BeanUtils.copyProperties(industry, response);
        response.setCertificateFiles(parseJsonArray(industry.getCertificateFiles(), String.class));
        response.setDownloadFileIds(parseJsonArray(industry.getDownloadFileIds(), Integer.class));
        return response;
    }

    @Override
    public void deleteByProductId(Integer productId) {
        LambdaQueryWrapper<StoreProductIndustry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreProductIndustry::getProductId, productId);
        remove(wrapper);
    }

    private StoreProductIndustry getByProductId(Integer productId) {
        LambdaQueryWrapper<StoreProductIndustry> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StoreProductIndustry::getProductId, productId);
        return getOne(wrapper);
    }

    private ProductIndustryResponse buildEmptyResponse() {
        ProductIndustryResponse response = new ProductIndustryResponse();
        response.setModelNo("");
        response.setVoltageLevel("");
        response.setConductorMaterial("");
        response.setCoreCount("");
        response.setCrossSectionArea("");
        response.setSheathMaterial("");
        response.setFlameRetardantGrade("");
        response.setStandardCode("");
        response.setApplicationScene("");
        response.setCertificateFiles(Collections.emptyList());
        response.setDownloadFileIds(Collections.emptyList());
        return response;
    }

    private <T> java.util.List<T> parseJsonArray(String json, Class<T> clazz) {
        if (json == null || json.isEmpty() || "[]".equals(json)) {
            return Collections.emptyList();
        }
        return JSON.parseArray(json, clazz);
    }
}
